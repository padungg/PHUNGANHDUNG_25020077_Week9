package com.lab;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShippingCalculatorTest {
    ShippingCalculator calc = new ShippingCalculator();
    
    @Test
    void testStandard() {
        assertEquals(15000.0, calc.calculate(5, "STANDARD"));
    }
    
    @Test
    void testExpress() {
        assertEquals(45000.0, calc.calculate(5, "EXPRESS"));
    }
    
    @Test
    void testInvalidWeight() {
        assertThrows(IllegalArgumentException.class,
            () -> calc.calculate(-1, "STANDARD"));
    }
    // Cố tình tạo lỗi
    @Test
    void testNullType() {
        // Cố tình truyền chữ null vào tham số type để hệ thống sập
        calc.calculate(5, null);
    }

}
