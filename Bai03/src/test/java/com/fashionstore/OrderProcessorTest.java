package com.fashionstore;

import com.odermanagement.Order;
import com.odermanagement.OrderProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrderProcessorTest {

    private OrderProcessor processor;

    @BeforeEach
    public void setUp() {
        processor = new OrderProcessor();
    }

    @Test
    public void testProcessValidOrder() {
        Order order = new Order("ORD-001", 150.0);
        boolean result = processor.processOrder(order);
        assertTrue(result, "Order should be processed successfully");
        assertTrue(order.isPaid(), "Order status should be paid");
    }

    @Test
    public void testProcessInvalidAmount() {
        Order order = new Order("ORD-002", -10.0);
        boolean result = processor.processOrder(order);
        assertFalse(result, "Order with negative amount should fail processing");
    }

    @Test
    public void testProcessNullOrder() {
        assertThrows(IllegalArgumentException.class, () -> {
            processor.processOrder(null);
        }, "Should throw exception when order is null");
    }
}
