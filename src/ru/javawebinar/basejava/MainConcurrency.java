package ru.javawebinar.basejava;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainConcurrency {
    public static final int THREAD_NUMBER = 10000;
    private static int counter = 0;
    private final AtomicInteger atomicInteger = new AtomicInteger();
    private static final Object LOCK = new Object();
    private static final Lock lock = new ReentrantLock();

    private static final SimpleDateFormat sdf = new SimpleDateFormat();

    private static final ThreadLocal<SimpleDateFormat> treadLocal = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat();
        }
    };

    String LOCK1 = "lock1";
    String LOCK2 = "lock2";

    public static void main(String[] args) throws InterruptedException {
        System.out.println(Thread.currentThread().getName());
        Thread thread0 = new Thread() {
            @Override
            public void run() {
                System.out.println(getName() + " " + getState());
            }
        };
        thread0.start();

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " " + Thread.currentThread().getState());
            }
        });
        thread1.start();

        System.out.println(thread0.getName() + " " + thread0.getState());

        final MainConcurrency mainConcurrency = new MainConcurrency();
        CountDownLatch latch = new CountDownLatch(THREAD_NUMBER);
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//        CompletionService completionService = new ExecutorCompletionService(executorService);

        List<Thread> threads = new ArrayList<>(THREAD_NUMBER);
        for (int i = 0; i < THREAD_NUMBER; i++) {
            Future<Integer> future = executorService.submit(() ->
//            Thread thread = new Thread(() ->
            {
                for (int j = 0; j < 100; j++) {
                    mainConcurrency.inc();
//                    System.out.println(sdf.format(new Date())); //Так делать нельзя! SimpleDateFormat не потокобезопасный!
                    System.out.println(treadLocal.get().format(new Date()));
                }
                latch.countDown();
                return 5;
            });

//            thread.start();
//            threads.add(thread);
        }
//        Thread.sleep(100);
        /*// Нужно подождать, пока звершатся все потоки, иначе main выведет счётчик раньше, а остальные потоки не успеют закончить счёт.
        // Поэтому ждём, пока все потоки закончат работу, используя join для каждого потока.
        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });*/

        latch.await(10, TimeUnit.SECONDS);
        executorService.shutdown();
//        System.out.println(counter);
        System.out.println(mainConcurrency.atomicInteger.get());

//        Thread.sleep(2000);
        System.out.println("Deadlock probe...:");

        Thread thrd1 = new Thread(new Runnable() {
            @Override
            public void run() {
                mainConcurrency.deadLock(mainConcurrency.LOCK1, mainConcurrency.LOCK2);
            }
        });

        Thread thrd2 = new Thread(new Runnable() {
            @Override
            public void run() {
                mainConcurrency.deadLock(mainConcurrency.LOCK2, mainConcurrency.LOCK1);
            }
        });
        thrd1.setName("Поток 1");
        thrd2.setName("Поток 2");
//        thrd1.start();
//        thrd2.start();
    }

    private void deadLock(Object a, Object b) {
        System.out.println(Thread.currentThread().getName() + " внутри метода");
        synchronized (a) {
            System.out.println("..." + Thread.currentThread().getName() + " занял объект " + a);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (b) {
                System.out.println("......" + Thread.currentThread().getName() + " занял объект " + b);
            }
        }
    }

    private void inc() {
//        double a = Math.pow(Math.sin(35), 3);
//        synchronized (this) {
//        lock.lock();
//        try {
        atomicInteger.incrementAndGet();
//            counter++;
//        } finally {
//            lock.unlock();
//        }
//        }
    }
}
