package com.dammnranaah.parkourplus;

import com.dammnranaah.parkourplus.command.ParkourCommand;
import com.dammnranaah.parkourplus.data.CourseManager;
import com.dammnranaah.parkourplus.data.LeaderboardService;
import com.dammnranaah.parkourplus.data.SessionManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ParkourPlus extends JavaPlugin {

    private CourseManager courseManager;
    private SessionManager sessionManager;
    private LeaderboardService leaderboardService;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.courseManager = new CourseManager(this);
        this.sessionManager = new SessionManager(this);
        this.leaderboardService = new LeaderboardService(this);

        ParkourCommand cmd = new ParkourCommand(this);
        if (getCommand("parkour") != null) {
            getCommand("parkour").setExecutor(cmd);
            getCommand("parkour").setTabCompleter(cmd);
        }
        getLogger().info("ParkourPlus enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("ParkourPlus disabled.");
    }

    public CourseManager getCourseManager() {
        return courseManager;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public LeaderboardService getLeaderboardService() {
        return leaderboardService;
    }
}
