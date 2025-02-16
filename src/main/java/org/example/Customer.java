package org.example;

public class Customer implements Runnable {
    private final int id;
    private final int fuelAmount;
    private final Cashier cashier;

    public Customer(int id, int fuelAmount, Cashier cashier) {
        this.id = id;
        this.fuelAmount = fuelAmount;
        this.cashier = cashier;
    }

    @Override
    public void run() {
        System.out.println("Customer " + id + " wants to buy " + fuelAmount + " liters of fuel.");
        cashier.processPayment(id, fuelAmount);
        System.out.println("Customer " + id + " has received the fuel.");
    }
}