package com.lab;

public class ShippingCalculator {
    public double calculate(double weight, String type) {
        if (weight <= 0) {
            // LỖI CỐ Ý 2: Xóa dấu ngắt chuỗi và nối chuỗi sai cú pháp (Compile Error)
            throw new IllegalArgumentException("Weight must be positive");
        }
        if ("EXPRESS".equals(type)) return weight * 5000 + 20000;
        if ("STANDARD".equals(type)) return weight * 3000;
        throw new IllegalArgumentException("Unknown type: " + type);
    }
}
