package com.banksystem;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lớp trừu tượng đại diện cho một tài khoản ngân hàng cơ bản.
 * Chứa các thông tin chung như số tài khoản, số dư và danh sách giao dịch.
 */
public abstract class Account {
  public static final String CHECKING_TYPE = "CHECKING";
  public static final String SAVINGS_TYPE = "SAVINGS";

  private static final Logger logger = LoggerFactory.getLogger(Account.class);

  private long accountNumber;
  private double balance;
  protected List<Transaction> transactionList;

  /**
   * Khởi tạo tài khoản với số tài khoản và số dư ban đầu.
   *
   * @param accountNumber số tài khoản
   * @param balance số dư ban đầu
   */
  public Account(long accountNumber, double balance) {
    this.accountNumber = accountNumber;
    this.balance = balance;
    this.transactionList = new ArrayList<>();
  }

  public long getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(long accountNumber) {
    this.accountNumber = accountNumber;
  }

  public double getBalance() {
    return balance;
  }

  protected void setBalance(double balance) {
    this.balance = balance;
  }

  public List<Transaction> getTransactionList() {
    return transactionList;
  }

  /**
   * Đặt danh sách giao dịch cho tài khoản.
   *
   * @param transactionList danh sách giao dịch cần đặt
   */
  public void setTransactionList(List<Transaction> transactionList) {
    if (transactionList == null) {
      this.transactionList = new ArrayList<>();
    } else {
      this.transactionList = transactionList;
    }
  }

  /**
   * Nạp tiền vào tài khoản.
   *
   * @param amount số tiền cần nạp
   */
  public abstract void deposit(double amount);

  /**
   * Rút tiền khỏi tài khoản.
   *
   * @param amount số tiền cần rút
   */
  public abstract void withdraw(double amount);

  protected void doDepositing(double amount) throws InvalidFundingAmountException {
    if (amount <= 0) {
      throw new InvalidFundingAmountException(amount);
    }
    balance += amount;
  }

  protected void doWithdrawing(double amount) throws BankException {
    if (amount <= 0) {
      throw new InvalidFundingAmountException(amount);
    }
    if (amount > balance) {
      throw new InsufficientFundsException(amount);
    }
    balance -= amount;
  }

  /**
   * Thêm một giao dịch vào danh sách giao dịch của tài khoản.
   *
   * @param transaction giao dịch cần thêm
   */
  public void addTransaction(Transaction transaction) {
    if (transaction != null) {
      transactionList.add(transaction);
    }
  }

  /**
   * Lấy lịch sử giao dịch của tài khoản dưới dạng chuỗi.
   *
   * @return chuỗi chứa lịch sử giao dịch
   */
  public String getTransactionHistory() {
    StringBuilder builder = new StringBuilder();
    builder.append("Lịch sử giao dịch của tài khoản ")
        .append(accountNumber)
        .append(":\n");
        
    for (int i = 0; i < transactionList.size(); i++) {
      builder.append(transactionList.get(i).getTransactionSummary());
      if (i < transactionList.size() - 1) {
        builder.append("\n");
      }
    }
    
    logger.info("Đã lấy lịch sử cho tài khoản: {}", accountNumber);
    return builder.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Account)) {
      return false;
    }
    Account other = (Account) obj;
    return this.accountNumber == other.accountNumber;
  }

  @Override
  public int hashCode() {
    return (int) (accountNumber ^ (accountNumber >>> 32));
  }
}