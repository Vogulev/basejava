package ru.javawebinar.basejava;

public class DeadLockTest {
    private static final Object LOCK1 = new Object();
    private static final Object LOCK2 = new Object();

    private void runThread1() {
        Thread thread1 = new Thread(() -> {
            synchronized (LOCK1) {
                System.out.println("thread1 удерживает LOCK1");
                System.out.println("thread1 ожидает LOCK2");
                synchronized (LOCK2) {
                    System.out.println("thread1 удерживает LOCK1 и LOCK2");
                }
            }
        });
        Thread thread2 = new Thread(() -> {
            synchronized (LOCK2) {
                System.out.println("thread2 удерживает LOCK2");
                System.out.println("thread2 ожидает LOCK1");
                synchronized (LOCK1) {
                    System.out.println("thread2 удерживает LOCK1 и LOCK2");
                }
            }
        });
        thread1.start();
        thread2.start();
    }

    public static void main(String[] args) {
        new DeadLockTest().runThread1();
    }
}
