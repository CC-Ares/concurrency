package main.java.com.ares.concurrency.atomic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * 让普通变量享受原子操作
 * 该类的作用是，当你想要将某类中的字段实现原子操作时，不需要将他修改为atmoicInteger类，而只需要将他该为volatile，然后使用updater操作
 */
public class AtomicIntegerFieldUpdaterDemo {
    public static class Candidate{
        int id;
        volatile int score; //还需要把int类型改为volatile
    }

    public final static AtomicIntegerFieldUpdater<Candidate> scoreUpdater = AtomicIntegerFieldUpdater.newUpdater(Candidate.class,"score");  //通过反射获取到Candidate的socre字段

    public static void main(String[] args) throws InterruptedException {
        final Candidate candidate= new Candidate();
        Thread[] ts = new Thread[10000];
        for(int i = 0;i<10000;i++){
            ts[i] = new Thread(){
                @Override
                public void run() {
                    scoreUpdater.incrementAndGet(candidate);   //针对candidate这个类中的score字段进行自增
                }
            };
            ts[i].start();
        }
        for(int i=0;i<10000;i++){
            ts[i].join();
        }
        System.out.println(candidate.score);
    }
}
