package com.geekbang.javatrain.week4.thread;

import java.util.concurrent.*;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 *
 * 一个简单的代码参考：
 */
public class SubThread06_CountDownLatch {

    public static void main(String[] args) throws InterruptedException {

        long start=System.currentTimeMillis();

        // 在这里创建一个线程或线程池，
        CountDownLatch countDownLatch = new CountDownLatch(1);
        MyCallable myCallable = new MyCallable(countDownLatch);
        FutureTask<Integer> futureTask = new FutureTask<>(myCallable);

        // 异步执行 下面方法
        new Thread(futureTask).start();
        countDownLatch.await(); // 注意跟CyclicBarrier不同，这里在主线程await

        try {
            // 确保  拿到result 并输出
            System.out.println("异步计算结果为："+futureTask.get(500, TimeUnit.MILLISECONDS));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        // 然后退出main线程
    }

    private static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if ( a < 2)
            return 1;
        return fibo(a-1) + fibo(a-2);
    }

    private static class MyCallable implements Callable<java.lang.Integer> {
        private CountDownLatch latch;
        public MyCallable(CountDownLatch latch){
            this.latch = latch;
        }
        @Override
        public java.lang.Integer call() {
            int sum = sum();
            latch.countDown();
            return sum;
        }
    }
}
