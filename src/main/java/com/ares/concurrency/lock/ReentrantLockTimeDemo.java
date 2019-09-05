package com.ares.concurrency.lock;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 可限时，当尝试时间到时，则返回false
 */
public class ReentrantLockTimeDemo implements Runnable{

    public static ReentrantLock reentrantLock = new ReentrantLock();

    @Override
    public void run() {
        try {
            if(reentrantLock.tryLock(5, TimeUnit.SECONDS)){
                Thread.sleep(6000);
            }else{
                System.out.println("get lock failed");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            if(reentrantLock.isHeldByCurrentThread()){
                reentrantLock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        ReentrantLockTimeDemo reentrantLockTimeDemo = new ReentrantLockTimeDemo();
        Thread thread = new Thread(reentrantLockTimeDemo);
        Thread thread1 = new Thread(reentrantLockTimeDemo);
        thread.start();
        thread1.start();

        System.out.println(Runtime.getRuntime().availableProcessors());
    }
}
