package com.banksystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lớp đại diện cho tài khoản tiết kiệm.
 * Thực thi các quy định về rút tiền và nạp tiền đặc thù của tài khoản tiết kiệm.
 */
public class SavingsAccount extends Account {

  private static final Logger logger = LoggerFactory.getLogger(SavingsAccount.class);
  private static final double MAX_WITHDRAW_AMOUNT = 1000.0;
  private static final double MIN_BALANCE_LIMIT = 5000.0;

  /**
   * Khởi tạo tài khoản tiết kiệm.
   *
   * @param accountNumber số tài khoản
   * @param balance số dư ban đầu
   */
  public SavingsAccount(long accountNumber, double balance) {
    super(accountNumber, balance);
  }

  @Override
  public void deposit(double amount) {
    logger.debug("Bắt đầu xử lý giao dịch nạp tiền tài khoản tiết kiệm...");
    double initialBalance = getBalance();
    try {
      doDepositing(amount);
      double finalBalance = getBalance();
      Transaction transaction = new Transaction(
          Transaction.TYPE_DEPOSIT_SAVINGS,
          amount,
          initialBalance,
          finalBalance);
      addTransaction(transaction);
      logger.info("Nạp tiền vào tài khoản tiết kiệm {} thành công: +{}", 
          getAccountNumber(), amount);
      System.out.println("Nap tien vao tai khoan " + getAccountNumber() 
          + " thanh cong: +" + amount);
    } catch (BankException e) {
      logger.error("Lỗi nạp tiền tài khoản tiết kiệm {}: {}", getAccountNumber(), e.getMessage());
      System.out.println("Loi nap tien: " + e.getMessage());
    }
  }

  @Override
  public void withdraw(double amount) {
    double initialBalance = getBalance();
    try {
      if (amount > MAX_WITHDRAW_AMOUNT) {
        throw new InvalidFundingAmountException(amount);
      }
      if (initialBalance - amount < MIN_BALANCE_LIMIT) {
        throw new InsufficientFundsException(amount);
      }

      doWithdrawing(amount);
      double finalBalance = getBalance();

      Transaction transaction = new Transaction(
          Transaction.TYPE_WITHDRAW_SAVINGS,
          amount,
          initialBalance,
          finalBalance);
      addTransaction(transaction);

      logger.info("Rút tiền tài khoản tiết kiệm {} thành công: -{}. Số dư còn: {}", 
          getAccountNumber(), amount, finalBalance);
      System.out.println("[SAVINGS] Rut " + amount + " thanh cong. So du con: " + finalBalance);
    } catch (BankException e) {
      logger.error("Lỗi rút tiền tài khoản tiết kiệm {}: {}", getAccountNumber(), e.getMessage());
      System.out.println("Rut tien bi loi!");
    }
  }
}