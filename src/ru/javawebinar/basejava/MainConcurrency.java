package ru.javawebinar.basejava;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainConcurrency {
    //    private static final Object LOCK = new Object();
    private static final Lock lock = new ReentrantLock();
    public static final int THREADS_NUMBER = 10000;
    private static int counter;
    private static final AtomicInteger atomicCounter = new AtomicInteger();
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public static void main(String[] args) throws InterruptedException {
        System.out.println(Thread.currentThread().getName());
        Thread thread0 = new Thread() {
            @Override
            public void run() {
                System.out.println(getName() + ", " + getState());
            }
        };
        thread0.start();

        new Thread(() -> System.out.println(Thread.currentThread().getName() + ", " + Thread.currentThread().getState())).start();

        System.out.println(thread0.getState());
        MainConcurrency mainConcurrency = new MainConcurrency();
        CountDownLatch latch = new CountDownLatch(THREADS_NUMBER);
        ExecutorService executorService = Executors.newCachedThreadPool();
//        List<Thread> threads = new ArrayList<>(THREADS_NUMBER);

        for (int i = 0; i < THREADS_NUMBER; i++) {
            Future<Integer> future = executorService.submit(() ->

//            Thread thread = new Thread(() ->

            {
                for (int j = 0; j < 100; j++) {
                    mainConcurrency.inc();
                    System.out.println(dateTimeFormatter.format(LocalDateTime.now()));
                }
                latch.countDown();
                return 5;
            });
/*            System.out.println(future.isDone());
            try {
                System.out.println(future.get());
            } catch (ExecutionException e) {
                e.printStackTrace();
            }*/
//            thread.start();
//            threads.add(thread);
        }

/*        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });*/
        latch.await(10, TimeUnit.SECONDS);
        executorService.shutdown();
//        System.out.println(counter);
        System.out.println(atomicCounter.get());
    }

    private void inc() {
  /*      lock.lock();
        try {
            counter++;
        } finally {
            lock.unlock();
        }*/
        atomicCounter.incrementAndGet();
    }
}