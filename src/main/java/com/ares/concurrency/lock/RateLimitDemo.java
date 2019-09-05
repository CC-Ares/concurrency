package com.ares.concurrency.lock;
import com.google.common.util.concurrent.RateLimiter;

public class RateLimitDemo {
     public static RateLimiter rateLimiter = RateLimiter.create(2);   //每秒产生两个令牌，即500毫秒产生一个

     public static class Task implements Runnable{

          @Override
          public void run() {
               System.out.println(System.currentTimeMillis());
          }
     }

     public static void main(String[] args) {
          for(int i =0;i<50;i++){
               rateLimiter.acquire();   //该方法会阻塞直到获取到令牌
//               if(!rateLimiter.tryAcquire()){     //该方法不会阻塞,如果拿不到令牌，则返回false
//                    continue;
//               }
               new Thread(new Task()).start();
          }
     }

}
