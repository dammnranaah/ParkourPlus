package com.dammnranaah.parkourplus.data;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Course {

    public enum Difficulty {EASY, MEDIUM, HARD}

    private final String name;
    private Difficulty difficulty;
    private Location start;
    private Location finish;
    private final List<Location> checkpoints = new ArrayList<>();

    public Course(String name, Difficulty difficulty) {
        this.name = name;
        this.difficulty = difficulty;
    }

    public String getName() {
        return name;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Location getStart() {
        return start;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public Location getFinish() {
        return finish;
    }

    public void setFinish(Location finish) {
        this.finish = finish;
    }

    public List<Location> getCheckpoints() {
        return checkpoints;
    }
}
