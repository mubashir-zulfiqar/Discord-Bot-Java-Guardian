//Developer: Mubashir Zulfiqar  Date: 2/27/2021  Time: 1:36 AM
//Happy Coding...

package me.deathvader.guardian_bot.command.lyrics;

public class StopWatch {
    private long startTime = 0;
    private long stopTime = 0;
    private boolean running = false;


    public void start() {
        this.startTime = System.nanoTime();
        this.running = true;
    }


    public void stop() {
        this.stopTime = System.nanoTime();
        this.running = false;
    }


    //elapsed time in milliseconds
    public long getElapsedTime() {
        long elapsed;
        if (running) {
            elapsed = (System.nanoTime() - startTime);
        } else {
            elapsed = (stopTime - startTime);
        }
        return elapsed;
    }


    //elapsed time in seconds
    public long getElapsedTimeSecs() {
        long elapsed;
        if (running) {
            elapsed = ((System.nanoTime() - startTime) / 1000);
        } else {
            elapsed = ((stopTime - startTime) / 1000);
        }
        return elapsed;
    }
}
