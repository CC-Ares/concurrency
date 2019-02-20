package main.java.com.ares.concurrency.atomic;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * ABA经典充值问题解决
 * 一开始充值线程获取到stamp为0，之后不管是充值线程还是用户线程对money做了操作，stamp都会+1，compare的时候，stamp不相同，则无法充值，如果stamp+1去掉，将会退化为普通atomic类，会一直充值
 */
public class AtomicStampReferenceDemo {
    static AtomicStampedReference<Integer> money = new AtomicStampedReference<Integer>(19,0);//初始值是19，初始stamp是0，每次修改可以让stamp增加，或者不改变

    public static void main(String[] args) {
        //模拟多个线程同时更新后台数据库，为用户充值
        for(int i = 0;i<3;i++){
            //注意，这里的timeStamp没有写在run方法里边，而是线程在一开始就获取到，后边只要被用户线程一修改，就无法再充值
            int timeStamp = money.getStamp();
            new Thread(){
                @Override
                public void run() {
                    while (true){
                        Integer m = money.getReference();
                        if(m<20){
                            if(money.compareAndSet(m,m+20,timeStamp,timeStamp+1)){
                                System.out.println("余额小于20，充值成功，余额：" + money.getReference() + "元");
                            }
                        }
                    }
                }
            }.start();
        }

        new Thread(){
            @Override
            public void run() {
                for(int i = 0;i<100;i++){
                    while (true){
                        //这里的stamp写在了run方法里边，因此线程每次执行都会更新stamp，因此都可以消费
                        int timeStamp = money.getStamp();
                        Integer m = money.getReference();
                        if(m>10){
                            System.out.println("大于10元");
                            if(money.compareAndSet(m,m-10,timeStamp,timeStamp+1)){
                                System.out.println("成功消费10元，余额："+money.getReference()+"元");
                                break;
                            }
                        }else {
                            System.out.println("没有足够的金额");
                            break;
                        }
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();
    }
}
