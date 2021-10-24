package ru.javawebinar.basejava;

import java.util.ArrayList;
import java.util.List;

public class MainConcurrency {
    private static int counter = 0;
    private static final Object LOCK = new Object();

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
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    mainConcurrency.inc();
                }
            });
            thread.start();
            threads.add(thread);
        }
//        Thread.sleep(100);
        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(counter);

        Thread.sleep(2000);
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
        thrd1.start();
        thrd2.start();
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

    private synchronized void inc() {
//        double a = Math.pow(Math.sin(35), 3);
//        synchronized (this) {
        counter++;
//        }
    }
}
