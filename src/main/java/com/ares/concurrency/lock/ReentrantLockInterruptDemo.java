package main.java.com.ares.concurrency.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 可中断
 */
public class ReentrantLockInterruptDemo implements Runnable{

    public static ReentrantLock reentrantLock1 = new ReentrantLock();
    public static ReentrantLock reentrantLock2 = new ReentrantLock();
    int lock;

    /**
     * 控制加锁顺序，方便造成死锁
     * @param lock
     */
    public ReentrantLockInterruptDemo(int lock){
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            if(lock == 1){
                reentrantLock1.lockInterruptibly();   //如果改成lock（），则即使调用了interrupt线程也不会中断
                Thread.sleep(500);
                reentrantLock2.lockInterruptibly();
            }else {
                reentrantLock2.lockInterruptibly();
                Thread.sleep(500);
                reentrantLock1.lockInterruptibly();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            //中断或者正常执行完之后，要把锁释放掉
            if(reentrantLock1.isHeldByCurrentThread()){
                reentrantLock1.unlock();
            }
            if(reentrantLock2.isHeldByCurrentThread()){
                reentrantLock2.unlock();
            }
            System.out.println(Thread.currentThread().getId() + "线程退出");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReentrantLockInterruptDemo reentrantLockInterruptDemo = new ReentrantLockInterruptDemo(1);
        ReentrantLockInterruptDemo reentrantLockInterruptDemo1 = new ReentrantLockInterruptDemo(2);
        Thread thread1 = new Thread(reentrantLockInterruptDemo);
        Thread thread2 = new Thread(reentrantLockInterruptDemo1);
        thread1.start();
        thread2.start();
        Thread.sleep(2000);
        thread1.interrupt();
    }
}
