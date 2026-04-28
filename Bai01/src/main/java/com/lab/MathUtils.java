package com.lab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MathUtils {
    private static final Logger logger = LoggerFactory.getLogger(MathUtils.class);

    public int add(int a, int b) {
        logger.info("Thực hiện cộng hai số: {} và {}", a, b);
        return a + b;
    }
}