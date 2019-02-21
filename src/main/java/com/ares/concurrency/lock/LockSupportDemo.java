package main.java.com.ares.concurrency.lock;

import java.util.concurrent.locks.LockSupport;

/**
 * 类似于suspend和resume的方法，但是不会使线程阻塞住，如果resume发生在suspend之前的话
 */
public class LockSupportDemo {

    static ChangeObjectThread changeObjectThread1 = new ChangeObjectThread("t1");
    static ChangeObjectThread changeObjectThread2 = new ChangeObjectThread("t2");
    public static Object object = new Object();

    public static class ChangeObjectThread extends Thread{
        public ChangeObjectThread(String name){
            super.setName(name);
        }

        @Override
        public void run() {
            synchronized (object){
                System.out.println("in "+getName());
                LockSupport.park();
                System.out.println(getName() + "unpark");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        changeObjectThread1.start();
        Thread.sleep(1000);
        changeObjectThread2.start();
        LockSupport.unpark(changeObjectThread1);
        LockSupport.unpark(changeObjectThread2);
        changeObjectThread1.join();
        changeObjectThread2.join();
    }
}
