package org.example;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Создаем кассира
        Cashier cashier = new Cashier();
        Thread cashierThread = new Thread(cashier);
        cashierThread.start();

        // Создаем насосы
        List<Pump> pumps = new ArrayList<>();
        List<Thread> pumpThreads = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Pump pump = new Pump(i, cashier);
            pumps.add(pump);
            Thread pumpThread = new Thread(pump);
            pumpThread.start();
            pumpThreads.add(pumpThread);
        }

        // Создаем покупателей
        List<Thread> customerThreads = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            Customer customer = new Customer(i, 100 + i * 5, cashier);
            Thread customerThread = new Thread(customer);
            customerThread.start();
            customerThreads.add(customerThread);
        }

        // Ожидаем завершения всех потоков покупателей
        for (Thread customerThread : customerThreads) {
            try {
                customerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Останавливаем кассира и насосы
        cashier.stop();
        for (Pump pump : pumps) {
            pump.stop();
        }

        System.out.println("Все покупатели обслужены. Программа завершена.");
    }
}