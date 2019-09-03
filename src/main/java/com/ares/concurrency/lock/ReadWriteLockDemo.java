package main.java.com.ares.concurrency.lock;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockDemo {
    private static Lock lock = new ReentrantLock();
    private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
    private static Lock readLock = reentrantReadWriteLock.readLock();
    private static Lock writeLock = reentrantReadWriteLock.writeLock();
    private int value;

    public Object handleRead(Lock lock) throws InterruptedException{
        try {
            lock.lock();                //模拟读操作
            Thread.sleep(1000);     //读操作的耗时越多，读写锁的优势就越明显
            return value;
        } finally {
            lock.unlock();
        }
    }

    public void handleWrite(Lock lock,int index) throws InterruptedException{
        try {
            lock.lock();
            Thread.sleep(1000);
            value = index;
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        final ReadWriteLockDemo readWriteLockDemo = new ReadWriteLockDemo();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try{
                    readWriteLockDemo.handleRead(readLock);
//                    lock.lock();  如果直接加普通锁，程序运行时间会从2秒增加到20秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                try {
                    readWriteLockDemo.handleWrite(writeLock,new Random().nextInt());
//                    lock.lock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        for(int i=0;i<18;i++){
            new Thread(runnable).start();
        }
        for(int i=18;i<20;i++){
            new Thread(runnable1).start();
        }
    }
}
