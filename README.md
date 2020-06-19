# redislockexperiment

# git
https://github.com/fw1036994377/redislockexperiment.git

# 疑问
volatile有屏障，cas有lock，synchronized有happensbefore监视器锁规则，
那么我们平时所说的redis锁，或者zookeeper锁，好像并没有一条happensbefore规则使前面的修改一定对后面的代码可见，那么假如有一个变量，定义在redis锁外，redis锁住的代码内部对其进行了修改，两条线程间的变量并不一定是可见的。

# 实验
```java
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

```

# 实验结果
等于10000，说明并没有出问题

# 想法
仍然不放弃这个数据可能有问题的疑问，或者说有某种机制保证了数据库的可见性，但是我还没掌握。如果有朋友知道问题的所在，希望在评论区指出，感谢。

