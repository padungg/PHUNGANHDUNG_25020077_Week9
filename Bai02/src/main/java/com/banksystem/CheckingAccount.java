package com.banksystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lớp đại diện cho tài khoản vãng lai.
 */
public class CheckingAccount extends Account {

  private static final Logger logger = LoggerFactory.getLogger(CheckingAccount.class);

  /**
   * Khởi tạo tài khoản vãng lai.
   *
   * @param accountNumber số tài khoản
   * @param balance số dư ban đầu
   */
  public CheckingAccount(long accountNumber, double balance) {
    super(accountNumber, balance);
  }

  @Override
  public void deposit(double amount) {
    double initialBalance = getBalance();
    try {
      doDepositing(amount);
      double finalBalance = getBalance();
      Transaction transaction = new Transaction(
          Transaction.TYPE_DEPOSIT_CHECKING,
          amount,
          initialBalance,
          finalBalance);
      addTransaction(transaction);
      logger.info("Nạp tiền vào tài khoản vãng lai {} thành công: +{}", 
          getAccountNumber(), amount);
    } catch (BankException e) {
      logger.error("Lỗi nạp tiền tài khoản vãng lai {}: {}", getAccountNumber(), e.getMessage());
      System.out.println(e.getMessage());
    }
  }

  @Override
  public void withdraw(double amount) {
    double initialBalance = getBalance();
    try {
      doWithdrawing(amount);
      double finalBalance = getBalance();
      Transaction transaction = new Transaction(
          Transaction.TYPE_WITHDRAW_CHECKING,
          amount,
          initialBalance,
          finalBalance);
      addTransaction(transaction);
      logger.info("Rút tiền từ tài khoản vãng lai {} thành công: -{}", 
          getAccountNumber(), amount);
    } catch (BankException e) {
      logger.error("Lỗi rút tiền tài khoản vãng lai {}: {}", getAccountNumber(), e.getMessage());
      System.out.println(e.getMessage());
    }
  }
}
