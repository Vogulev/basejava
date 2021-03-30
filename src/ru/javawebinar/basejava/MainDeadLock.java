package ru.javawebinar.basejava;

public class MainDeadLock {
    private static final String LOCK1 = "Lock1";
    private static final String LOCK2 = "Lock2";


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
