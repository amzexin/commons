package io.github.timeway.hbasedemo;

/**
 * Main
 *
 * @author Zexin Li
 * @date 2023-03-01 15:08
 */
public class Main {

    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        System.out.println("sleeping...");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        boolean parentThreadIsDaemon = Thread.currentThread().isDaemon();
        System.out.println("parentThreadIsDaemon = " + parentThreadIsDaemon);

        thread.setDaemon(false);
        thread.start();
    }
}
