package org.example;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class Pump implements Runnable {
    private final int id;
    private final Cashier cashier;
    private final Lock lock = new ReentrantLock();
    private final Condition cvPump = lock.newCondition();
    private boolean isRunning = true;

    public Pump(int id, Cashier cashier) {
        this.id = id;
        this.cashier = cashier;
    }

    @Override
    public void run() {
        while (isRunning) {
            lock.lock();
            try {
                cvPump.await(); // Ожидаем активации кассиром
                if (isRunning) {
                    System.out.println("Pump " + id + " is dispensing fuel.");
                    Thread.sleep(200); // Имитация заправки
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public void stop() {
        isRunning = false;
        lock.lock();
        try {
            cvPump.signal(); // Пробуждаем насос для завершения
        } finally {
            lock.unlock();
        }
    }
}