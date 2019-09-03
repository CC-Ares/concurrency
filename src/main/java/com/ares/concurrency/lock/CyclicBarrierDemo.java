package main.java.com.ares.concurrency.lock;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {
    public static class Soldier implements Runnable{

        private String soldierName;
        //每个士兵要持有同一个栅栏
        private final CyclicBarrier cyclicBarrier;

        public Soldier(String soldierName, CyclicBarrier cyclicBarrier) {
            this.soldierName = soldierName;
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {

            try {
                cyclicBarrier.await();//等待所有士兵到达
                dowork();
                cyclicBarrier.await();//等待所有士兵完成工作
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }


        }

        private void dowork() throws InterruptedException {
            Thread.sleep(Math.abs(new Random().nextInt()%10000));
            System.out.println(soldierName + ":任务完成");
        }
    }

    //该线程是所有士兵到达栅栏后将要执行的操作
    public static class BarrierRun implements Runnable{

        boolean flag;
        int N;

        public BarrierRun(boolean flag, int n) {
            this.flag = flag;
            N = n;
        }

        @Override
        public void run() {
            if(flag){
                System.out.println("士兵" + N + "个，任务完成");
            }else{
                System.out.println("士兵" + N + "个，集合完毕");
                flag = true;
            }
        }
    }

    public static void main(String[] args) {
        final  int N = 10;
        Thread[] allSoldier = new Thread[N];
        boolean flag = false;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(N,new BarrierRun(flag,N));
        System.out.println("集合队伍！");
        for(int i=0;i<N;i++){
            System.out.println("士兵"+i+"报道！");
            allSoldier[i]= new Thread(new Soldier("士兵 "+ i,cyclicBarrier));
            allSoldier[i].start();
        }
    }
}
