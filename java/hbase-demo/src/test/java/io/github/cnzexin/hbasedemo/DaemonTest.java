package io.github.cnzexin.hbasedemo;

import org.junit.Test;

/**
 * DaemonTest
 *
 * @author Zexin Li
 * @date 2023-03-01 15:04
 */
public class DaemonTest {

    @Test
    public void main() {
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
        thread.start();

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("准备关闭...");
                    Thread.sleep(1000);
                    System.out.println("关闭成功...");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }));
    }
}
