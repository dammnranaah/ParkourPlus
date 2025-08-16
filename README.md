# ParkourPlus

Adds timed parkour courses to your server with leaderboards, rewards, and challenges.

## Build
- Requirements: Java 21, Maven 3.9+
- Build: `mvn -q -DskipTests package`
- Output: `target/parkourplus-0.1.0-shaded.jar` (shaded) or normal jar if you remove shade plugin.

## Install
- Copy the jar into your Paper 1.21.1 server's `plugins/` folder.
- Start the server.

## Commands
- `/parkour start <course>`
- `/parkour leave`
- `/parkour stats`
- `/parkour leaderboard <course>`
- `/parkour create <course> [EASY|MEDIUM|HARD]`
- `/parkour setcheckpoint` (admin WIP)

## Notes
This is a minimal scaffold with in-memory data. Persistence, regions, checkpoints logic, rewards, and events are WIP.
