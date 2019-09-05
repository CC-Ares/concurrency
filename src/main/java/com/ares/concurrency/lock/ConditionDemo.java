package com.ares.concurrency.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 功能类似于synchronize中的wait和notify
 * condition.await
 * condition.signal
 */
public class ConditionDemo implements Runnable{

    public static ReentrantLock reentrantLock = new ReentrantLock();
    public static Condition condition = reentrantLock.newCondition();
    @Override
    public void run() {
        reentrantLock.lock();
        try {
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Thread is going on ");
    }

    public static void main(String[] args) throws InterruptedException {
        ConditionDemo conditionDemo = new ConditionDemo();
        Thread thread = new Thread(conditionDemo);
        thread.start();
        Thread.sleep(2000);
        reentrantLock.lock();
        condition.signal();     //condition await 和 signal之前都必须要先获得锁 signal唤醒之后，原来的线程不是马上执行，而是必须等到重新拿到锁之后才会往下执行
        reentrantLock.unlock();
    }
}
