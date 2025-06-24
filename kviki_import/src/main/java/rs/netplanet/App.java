package rs.netplanet;

import java.util.Timer;

import rs.netplanet.Threads.MainThread;

public class App {
    public static void main(String[] args) {
        Timer timer = new Timer();
        MainThread mainThread = new MainThread();

        // timer.schedule(mainThread, 0, 5000);
        timer.schedule(mainThread, 0, 3 * 60 * 1000);
    }
}
