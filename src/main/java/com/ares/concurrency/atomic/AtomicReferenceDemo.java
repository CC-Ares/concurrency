package main.java.com.ares.concurrency.atomic;

import java.util.concurrent.atomic.AtomicReference;

/**
 * atomicReference类可以使用compareAndSet保证线程的安全性，只有跟预期值相同才会修改
 * 但是会出现ABA问题，可以用atomicStampReference解决
 */
public class AtomicReferenceDemo {
    static AtomicReference<String> stringAtomicReference = new AtomicReference<String>("abc");

    public static class ChangeThread implements Runnable{

        @Override
        public void run() {
            try {
                Thread.sleep(Math.abs((int)(Math.random()*100)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(stringAtomicReference.compareAndSet("abc","def")){
                System.out.println("Thread" + Thread.currentThread().getId()+" change success");
            }else{
                System.out.println("Thread" + Thread.currentThread().getId()+" change failed");
            }
        }
    }

    public static void main(String[] args) {
        Thread [] ts = new Thread[10];
        for(int i = 0;i<10;i++){
            ts[i] = new Thread(new ChangeThread());
        }
        for(int i = 0;i<10;i++){
            ts[i].start();
        }
    }
}
