package com.odermanagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderProcessor {
    private static final Logger logger = LoggerFactory.getLogger(OrderProcessor.class);

    public boolean processOrder(Order order) {
        if (order == null) {
            logger.error("Order processing failed: Order is null");
            throw new IllegalArgumentException("Order cannot be null");
        }

        logger.info("Starting processing for Order ID: {}", order.getId());

        if (order.getAmount() <= 0) {
            logger.warn("Order ID: {} has an invalid amount: {}", order.getId(), order.getAmount());
            return false;
        }

        if (order.isPaid()) {
            logger.info("Order ID: {} is already paid.", order.getId());
            return true;
        }

        // Simulate payment processing
        logger.debug("Processing payment for Order ID: {} with amount: {}", order.getId(), order.getAmount());
        order.setPaid(true);
        logger.info("Successfully processed Order ID: {}", order.getId());

        return true;
    }
}
