package ru.javawebinar.basejava;

public class MainDeadLock {
    private static int counter;
    private static final Object LOCK1 = new Object();
    private static final Object LOCK2 = new Object();


    public static void main(String[] args) {
            getDeadLock(LOCK1, LOCK2);
            getDeadLock(LOCK2, LOCK1);
    }

    private static void getDeadLock(Object lock1, Object lock2) {
        new Thread(() -> {
            synchronized (lock1) {
                System.out.println(Thread.currentThread().getName() + " gets lock on " + lock1);
                System.out.println(Thread.currentThread().getName() + " waits for " + lock2);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock2) {
                    System.out.println(Thread.currentThread().getName() + " gets lock on " + lock1 + " and " + lock2);
                }
            }
        }).start();
    }
}
