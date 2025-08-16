package com.dammnranaah.parkourplus.data;

import com.dammnranaah.parkourplus.ParkourPlus;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class CourseManager {

    private final ParkourPlus plugin;
    private final Map<String, Course> courses = new HashMap<>();
    private final Map<UUID, String> adminSelectedCourse = new HashMap<>();

    public CourseManager(ParkourPlus plugin) {
        this.plugin = plugin;
    }

    public Course createCourse(String name, Course.Difficulty difficulty) {
        String key = name.toLowerCase();
        if (courses.containsKey(key)) return null;
        Course c = new Course(name, difficulty);
        courses.put(key, c);
        return c;
    }

    public Course getCourse(String name) {
        if (name == null) return null;
        return courses.get(name.toLowerCase());
    }

    public List<String> listCourses() {
        return new ArrayList<>(courses.values().stream().map(Course::getName).toList());
    }

    public void selectCourseForAdmin(Player p, String name) {
        adminSelectedCourse.put(p.getUniqueId(), name.toLowerCase());
    }

    public String getSelectedCourse(Player p) {
        return adminSelectedCourse.get(p.getUniqueId());
    }

    public boolean addCheckpointAtPlayerLocation(Player p) {
        String sel = getSelectedCourse(p);
        if (sel == null) return false;
        Course c = courses.get(sel);
        if (c == null) return false;
        Location loc = p.getLocation().clone();
        c.getCheckpoints().add(loc);
        return true;
    }

    public boolean setStartAtPlayerLocation(Player p) {
        String sel = getSelectedCourse(p);
        if (sel == null) return false;
        Course c = courses.get(sel);
        if (c == null) return false;
        c.setStart(p.getLocation().clone());
        return true;
    }

    public boolean setFinishAtPlayerLocation(Player p) {
        String sel = getSelectedCourse(p);
        if (sel == null) return false;
        Course c = courses.get(sel);
        if (c == null) return false;
        c.setFinish(p.getLocation().clone());
        return true;
    }
}
