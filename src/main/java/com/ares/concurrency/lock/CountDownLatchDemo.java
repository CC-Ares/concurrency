package main.java.com.ares.concurrency.lock;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 可以在主线程设置countdown数量，调用await方法，只有当从线程将countdown数量减为0的时候，主线程才会继续执行
 * 还有一种使用场景，将countdown设置为1，在所有的从线程调用await方法，在主线程调用countdown方法，可以十线所有子线程同时执行
 */
public class CountDownLatchDemo implements Runnable {
    static final CountDownLatch countDownLatch = new CountDownLatch(10);
    static final CountDownLatchDemo countDownLatchDemo = new CountDownLatchDemo();

    @Override
    public void run() {
        try {
            Thread.sleep(new Random().nextInt(10)*1000);
            System.out.println("check complete");
            countDownLatch.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for(int i=0;i<10;i++){
            executorService.execute(countDownLatchDemo);
        }
        countDownLatch.await();
        System.out.println("main completed");
    }
}
