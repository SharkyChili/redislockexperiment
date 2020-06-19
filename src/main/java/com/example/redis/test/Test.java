package com.example.redis.test;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Test {
    public static Integer i = 0;
    public static JedisClient jedisClient = new JedisClient();

    public static void main(String[] args) throws InterruptedException {
        int count = 10000;
        CountDownLatch countDownLatch = new CountDownLatch(count);
        for (int i1 = 0; i1 < count; i1++) {
            new Thread(new Worker(countDownLatch)).start();
        }

        countDownLatch.await();
        System.out.println(i);
    }

    static class Worker implements Runnable{
        CountDownLatch latch;
        Worker(CountDownLatch latch){
            this.latch = latch;
        }

        @Override
        public void run() {
            String key ="myKey";
            Random random = new Random();
            String value = String.valueOf(random.nextInt());
            try{
                while(true){
                    boolean b = jedisClient.tryGetDistributedLock(key, value, 1000);
                    if(b) {
                        i++;
                        break;
                    }
                }
            }finally {
                jedisClient.releaseDistributedLock(key,value);
            }
            latch.countDown();
        }
    }

}
