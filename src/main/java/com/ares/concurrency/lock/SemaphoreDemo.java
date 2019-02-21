package main.java.com.ares.concurrency.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 共享锁，可以让多个线程同时获取到锁，通过信号量控制线程的数量
 */
public class SemaphoreDemo implements Runnable{
    public static Semaphore semaphore = new Semaphore(10);

    @Override
    public void run() {
        try {
            semaphore.acquire(1);       //也可以一次获取多个信号量
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getId() + " done!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            semaphore.release();            //也可以一次释放多个信号量
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        final SemaphoreDemo semaphoreDemo =new SemaphoreDemo();
        for(int i = 0;i<20;i++){
            executorService.execute(semaphoreDemo);
        }
    }
}
