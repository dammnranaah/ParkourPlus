package com.dammnranaah.parkourplus.data;

import com.dammnranaah.parkourplus.ParkourPlus;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {

    public static class RunSession {
        public final String courseName;
        public final Instant startTime;
        public int checkpointIndex = -1;
        public Location lastCheckpoint;

        public RunSession(String courseName, Instant startTime) {
            this.courseName = courseName;
            this.startTime = startTime;
        }
    }

    private final ParkourPlus plugin;
    private final Map<UUID, RunSession> active = new HashMap<>();

    public SessionManager(ParkourPlus plugin) {
        this.plugin = plugin;
    }

    public void startRun(Player p, Course course) {
        RunSession s = new RunSession(course.getName(), Instant.now());
        s.lastCheckpoint = course.getStart();
        active.put(p.getUniqueId(), s);
        if (course.getStart() != null) {
            p.teleport(course.getStart());
        }
    }

    public void leave(Player p) {
        active.remove(p.getUniqueId());
    }

    public Duration finish(Player p) {
        RunSession s = active.remove(p.getUniqueId());
        if (s == null) return null;
        return Duration.between(s.startTime, Instant.now());
    }

    public void checkpoint(Player p, Location loc, int index) {
        RunSession s = active.get(p.getUniqueId());
        if (s != null) {
            s.lastCheckpoint = loc;
            s.checkpointIndex = index;
        }
    }

    public RunSession getSession(Player p) {
        return active.get(p.getUniqueId());
    }
}
