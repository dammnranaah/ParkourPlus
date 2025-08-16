package com.dammnranaah.parkourplus.data;

import com.dammnranaah.parkourplus.ParkourPlus;

import java.time.Duration;
import java.util.*;

public class LeaderboardService {

    public static class Record implements Comparable<Record> {
        public final UUID player;
        public final Duration time;

        public Record(UUID player, Duration time) {
            this.player = player;
            this.time = time;
        }

        @Override
        public int compareTo(Record o) {
            int cmp = this.time.compareTo(o.time);
            if (cmp != 0) return cmp;
            return this.player.compareTo(o.player);
        }
    }

    private final ParkourPlus plugin;
    private final Map<String, NavigableSet<Record>> boards = new HashMap<>();

    public LeaderboardService(ParkourPlus plugin) {
        this.plugin = plugin;
    }

    public void addRecord(String courseName, UUID player, Duration time) {
        String key = courseName.toLowerCase();
        boards.computeIfAbsent(key, k -> new TreeSet<>()).add(new Record(player, time));
    }

    public List<Record> top(String courseName, int limit) {
        String key = courseName.toLowerCase();
        NavigableSet<Record> set = boards.getOrDefault(key, new TreeSet<>());
        List<Record> list = new ArrayList<>(set);
        return list.subList(0, Math.min(limit, list.size()));
    }

    public Optional<Duration> bestFor(String courseName, UUID player) {
        String key = courseName.toLowerCase();
        NavigableSet<Record> set = boards.get(key);
        if (set == null) return Optional.empty();
        return set.stream().filter(r -> r.player.equals(player)).map(r -> r.time).min(Duration::compareTo);
    }
}
