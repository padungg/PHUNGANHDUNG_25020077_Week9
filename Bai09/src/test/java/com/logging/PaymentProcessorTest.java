package com.logging;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PaymentProcessorTest {
    
    @Test
    public void testValidPayment() {
        PaymentProcessor processor = new PaymentProcessor();
        // Sẽ sinh ra 2 dòng log INFO
        boolean result = processor.processPayment("ORD-12345", 500.0);
        assertTrue(result);
    }

    @Test
    public void testInvalidPayment() {
        PaymentProcessor processor = new PaymentProcessor();
        // Cố tình truyền số tiền âm để bắt exception và sinh ra log ERROR
        boolean result = processor.processPayment("ORD-ERROR", -100.0);
        assertFalse(result);
    }
}
