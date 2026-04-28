package com.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaymentProcessor {
    // Khởi tạo Logger từ giao diện SLF4J
    private static final Logger logger = LoggerFactory.getLogger(PaymentProcessor.class);

    public boolean processPayment(String orderId, double amount) {
        // TỐI ƯU 1: Xóa bỏ hoàn toàn System.out.println()
        // TỐI ƯU 2: Sử dụng Parameterized Logging {} để tối ưu hiệu suất ghép chuỗi
        logger.info("Bắt đầu xử lý thanh toán cho đơn hàng ID: {}, Số tiền: {} VND", orderId, amount);

        try {
            if (amount <= 0) {
                throw new IllegalArgumentException("Số tiền thanh toán phải lớn hơn 0!");
            }
            
            // Giả lập logic thanh toán thành công
            logger.info("Hoàn tất: Thanh toán thành công cho đơn hàng ID: {}", orderId);
            return true;

        } catch (Exception e) {
            // TỐI ƯU 3: Sử dụng mức độ ERROR và đính kèm Exception e để in ra dấu vết lỗi (Stacktrace)
            logger.error("LỖI NGHIÊM TRỌNG khi xử lý thanh toán cho đơn hàng {}. Nguyên nhân: {}", orderId, e.getMessage(), e);
            return false;
        }
    }
}
