package com.dammnranaah.parkourplus.command;

import com.dammnranaah.parkourplus.ParkourPlus;
import com.dammnranaah.parkourplus.data.Course;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ParkourCommand implements CommandExecutor, TabCompleter {

    private final ParkourPlus plugin;

    public ParkourCommand(ParkourPlus plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "Usage: /" + label + " <start|leave|stats|leaderboard|create|select|setstart|setfinish|setcheckpoint> [args]");
            return true;
        }
        String sub = args[0].toLowerCase();
        switch (sub) {
            case "start":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Only players can start a course.");
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /" + label + " start <course>");
                    return true;
                }
                String startCourse = args[1];
                Course course = plugin.getCourseManager().getCourse(startCourse);
                if (course == null) {
                    sender.sendMessage(ChatColor.RED + "Course not found: " + startCourse);
                    return true;
                }
                plugin.getSessionManager().startRun((Player) sender, course);
                sender.sendMessage(ChatColor.GREEN + "Started course: " + course.getName());
                return true;
            case "leave":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Only players can leave a course.");
                    return true;
                }
                plugin.getSessionManager().leave((Player) sender);
                sender.sendMessage(ChatColor.YELLOW + "You left the current course.");
                return true;
            case "stats":
                // Placeholder for player stats
                sender.sendMessage(ChatColor.AQUA + "Your best times: (coming soon)");
                return true;
            case "leaderboard":
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /" + label + " leaderboard <course>");
                    return true;
                }
                String lbCourse = args[1];
                sender.sendMessage(ChatColor.GOLD + "Top 10 for " + lbCourse + ": (coming soon)");
                return true;
            case "create":
                if (!sender.hasPermission("parkourplus.admin")) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission.");
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /" + label + " create <course> [difficulty]");
                    return true;
                }
                String name = args[1];
                Course.Difficulty diff = Course.Difficulty.MEDIUM;
                if (args.length >= 3) {
                    try {
                        diff = Course.Difficulty.valueOf(args[2].toUpperCase());
                    } catch (IllegalArgumentException ignored) {
                        sender.sendMessage(ChatColor.RED + "Invalid difficulty. Use EASY, MEDIUM, HARD.");
                        return true;
                    }
                }
                Course created = plugin.getCourseManager().createCourse(name, diff);
                if (created == null) {
                    sender.sendMessage(ChatColor.RED + "Course already exists: " + name);
                } else {
                    sender.sendMessage(ChatColor.GREEN + "Created course '" + name + "' with difficulty " + diff);
                }
                return true;
            case "select":
                if (!sender.hasPermission("parkourplus.admin")) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission.");
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /" + label + " select <course>");
                    return true;
                }
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Only players can select a course for editing.");
                    return true;
                }
                String selName = args[1];
                Course selCourse = plugin.getCourseManager().getCourse(selName);
                if (selCourse == null) {
                    sender.sendMessage(ChatColor.RED + "Course not found: " + selName);
                    return true;
                }
                plugin.getCourseManager().selectCourseForAdmin((Player) sender, selCourse.getName());
                sender.sendMessage(ChatColor.GREEN + "Selected course '" + selCourse.getName() + "' for editing.");
                return true;
            case "setstart":
                if (!sender.hasPermission("parkourplus.admin")) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission.");
                    return true;
                }
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Only players can set start.");
                    return true;
                }
                if (plugin.getCourseManager().setStartAtPlayerLocation((Player) sender)) {
                    sender.sendMessage(ChatColor.GREEN + "Set start location for selected course.");
                } else {
                    sender.sendMessage(ChatColor.RED + "No selected course or course missing. Use /" + label + " select <course>.");
                }
                return true;
            case "setfinish":
                if (!sender.hasPermission("parkourplus.admin")) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission.");
                    return true;
                }
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Only players can set finish.");
                    return true;
                }
                if (plugin.getCourseManager().setFinishAtPlayerLocation((Player) sender)) {
                    sender.sendMessage(ChatColor.GREEN + "Set finish location for selected course.");
                } else {
                    sender.sendMessage(ChatColor.RED + "No selected course or course missing. Use /" + label + " select <course>.");
                }
                return true;
            case "setcheckpoint":
                if (!sender.hasPermission("parkourplus.admin")) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission.");
                    return true;
                }
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Only players can set checkpoints.");
                    return true;
                }
                Player p = (Player) sender;
                boolean ok = plugin.getCourseManager().addCheckpointAtPlayerLocation(p);
                if (ok) {
                    sender.sendMessage(ChatColor.GREEN + "Added checkpoint at your location to the selected course (WIP).");
                } else {
                    sender.sendMessage(ChatColor.RED + "No course selected to add checkpoint. (WIP)");
                }
                return true;
            default:
                sender.sendMessage(ChatColor.YELLOW + "Unknown subcommand.");
                return true;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return Arrays.asList("start", "leave", "stats", "leaderboard", "create", "select", "setstart", "setfinish", "setcheckpoint");
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("start")) {
            return plugin.getCourseManager().listCourses();
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("leaderboard")) {
            return plugin.getCourseManager().listCourses();
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("select")) {
            return plugin.getCourseManager().listCourses();
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("create")) {
            return Arrays.stream(Course.Difficulty.values()).map(Enum::name).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
