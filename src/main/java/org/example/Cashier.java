package org.example;

import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class Cashier implements Runnable {
    private final Queue<Integer> customerQueue = new LinkedList<>();
    private final Lock lock = new ReentrantLock();
    private final Condition cvCashier = lock.newCondition();
    private final Condition cvPump = lock.newCondition();
    private boolean isRunning = true;

    public void processPayment(int customerId, int fuelAmount) {
        lock.lock();
        try {
            customerQueue.add(fuelAmount);
            cvCashier.signal(); // Уведомляем кассира о новом покупателе
            cvPump.await(); // Ожидаем завершения заправки
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            lock.lock();
            try {
                while (customerQueue.isEmpty() && isRunning) {
                    cvCashier.await(); // Ожидаем новых покупателей
                }
                if (!customerQueue.isEmpty()) {
                    int fuelAmount = customerQueue.poll();
                    System.out.println("Cashier is processing payment for " + fuelAmount + " liters.");
                    Thread.sleep(100); // Имитация обработки оплаты
                    cvPump.signal(); // Уведомляем насос о готовности
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
            cvCashier.signalAll(); // Пробуждаем кассира для завершения
            cvPump.signalAll(); // Пробуждаем насосы для завершения
        } finally {
            lock.unlock();
        }
    }
}