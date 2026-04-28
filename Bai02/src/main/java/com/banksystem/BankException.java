package com.banksystem;

/**
 * Ngoại lệ chung trong hệ thống ngân hàng.
 */
public class BankException extends Exception {
  /**
   * Khởi tạo ngoại lệ ngân hàng với thông báo lỗi.
   *
   * @param message thông báo lỗi
   */
  public BankException(String message) {
    super(message);
  }
}
