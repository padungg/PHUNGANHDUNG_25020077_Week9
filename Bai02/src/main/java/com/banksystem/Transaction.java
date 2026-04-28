package com.banksystem;

import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lớp đại diện cho một giao dịch trong hệ thống ngân hàng.
 */
public class Transaction {
  public static final int TYPE_DEPOSIT_CHECKING = 1;
  public static final int TYPE_WITHDRAW_CHECKING = 2;
  public static final int TYPE_DEPOSIT_SAVINGS = 3;
  public static final int TYPE_WITHDRAW_SAVINGS = 4;

  private static final Logger logger = LoggerFactory.getLogger(Transaction.class);

  private int type;
  private double amount;
  private double initialBalance;
  private double finalBalance;

  /**
   * Khởi tạo một giao dịch mới.
   *
   * @param type loại giao dịch
   * @param amount số tiền giao dịch
   * @param initialBalance số dư ban đầu
   * @param finalBalance số dư sau giao dịch
   */
  public Transaction(int type, double amount, double initialBalance, double finalBalance) {
    this.type = type;
    this.amount = amount;
    this.initialBalance = initialBalance;
    this.finalBalance = finalBalance;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public double getInitialBalance() {
    return initialBalance;
  }

  public void setInitialBalance(double initialBalance) {
    this.initialBalance = initialBalance;
  }

  public double getFinalBalance() {
    return finalBalance;
  }

  public void setFinalBalance(double finalBalance) {
    this.finalBalance = finalBalance;
  }

  /**
   * Lấy chuỗi mô tả cho loại giao dịch.
   *
   * @param transactionType mã loại giao dịch
   * @return chuỗi mô tả tương ứng
   */
  public static String getTypeString(int transactionType) {
    switch (transactionType) {
      case TYPE_DEPOSIT_CHECKING:
        return "Nạp tiền vãng lai";
      case TYPE_WITHDRAW_CHECKING:
        return "Rút tiền vãng lai";
      case TYPE_DEPOSIT_SAVINGS:
        return "Nạp tiền tiết kiệm";
      case TYPE_WITHDRAW_SAVINGS:
        return "Rút tiền tiết kiệm";
      default:
        return "Không rõ";
    }
  }

  /**
   * Lấy bản tóm tắt thông tin của giao dịch.
   *
   * @return chuỗi tóm tắt thông tin giao dịch
   */
  public String getTransactionSummary() {
    logger.debug("Bắt đầu xử lý tạo tóm tắt giao dịch cho loại: {}", type);

    String formattedInitial = String.format(Locale.US, "%.2f", initialBalance);
    String formattedAmount = String.format(Locale.US, "%.2f", amount);
    String formattedFinal = String.format(Locale.US, "%.2f", finalBalance);

    StringBuilder builder = new StringBuilder();
    builder.append("- Kiểu giao dịch: ")
        .append(getTypeString(type))
        .append(". Số dư ban đầu: $")
        .append(formattedInitial)
        .append(". Số tiền: $")
        .append(formattedAmount)
        .append(". Số dư cuối: $")
        .append(formattedFinal)
        .append(".");

    return builder.toString();
  }
}
