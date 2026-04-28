package com.odermanagement;

public class Order {
    private String id;
    private double amount;
    private boolean isPaid;

    public Order(String id, double amount) {
        this.id = id;
        this.amount = amount;
        this.isPaid = false;
    }

    public String getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}
