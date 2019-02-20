package main.java.com.ares.concurrency.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * atomicInteger可以解决在多线程环境下，由于int类型的操作非原子性，而导致的结果错误问题
 * 如果使用int，结果小于100000
 */
public class AtomicIntegerDemo {

    static AtomicInteger i = new AtomicInteger();

    public static class AddThread implements Runnable{

        @Override
        public void run() {
            for(int k = 0;k<10000;k++){
                i.incrementAndGet();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread[] ts = new Thread[10];
        for(int i = 0;i<10;i++){
            ts[i] = new Thread(new AddThread());
        }
        for(int i = 0;i<10;i++){
            ts[i].start();
        }for(int i = 0;i<10;i++){
            ts[i].join();
        }
        System.out.println(i);
    }
}
