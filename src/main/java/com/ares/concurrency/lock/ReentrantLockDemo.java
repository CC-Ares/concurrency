package main.java.com.ares.concurrency.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * reentrantLock 4个特性
 * 1.可重入
 * 2.可中断
 * 3.可限时
 * 4.公平锁
 */
public class ReentrantLockDemo implements Runnable{
    public static ReentrantLock lock = new ReentrantLock();
    public static int i=0;
    @Override
    public void run() {
        for(int j=0;j<100000;j++){
            lock.lock();
            lock.lock();
            try{
                i++;
            }
            finally {
                lock.unlock();
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReentrantLockDemo reentrantLockDemo = new ReentrantLockDemo();
        Thread thread = new Thread(reentrantLockDemo);
        Thread thread1 = new Thread(reentrantLockDemo);
        thread.start();
        thread1.start();
        thread.join();
        thread1.join();
        System.out.println(i);
    }
}
