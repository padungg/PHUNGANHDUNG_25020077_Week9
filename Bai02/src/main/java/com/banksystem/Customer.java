package com.banksystem;

import java.util.ArrayList;
import java.util.List;

/**
 * Lớp Customer đại diện cho một khách hàng.
 */
public class Customer {
  private long idNumber;
  private String fullName;
  private List<Account> accountList;

  /**
   * Constructor không tham số (phục vụ mục đích test hoặc default instantiation).
   */
  public Customer() {
    this(0L, "");
  }

  /**
   * Khởi tạo khách hàng với CMND và họ tên.
   *
   * @param idNumber số CMND
   * @param fullName họ tên khách hàng
   */
  public Customer(long idNumber, String fullName) {
    this.idNumber = idNumber;
    this.fullName = fullName;
    this.accountList = new ArrayList<>();
  }

  public long getIdNumber() {
    return idNumber;
  }

  public void setIdNumber(long idNumber) {
    this.idNumber = idNumber;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public List<Account> getAccountList() {
    return accountList;
  }

  /**
   * Đặt danh sách tài khoản cho khách hàng.
   *
   * @param accountList danh sách tài khoản
   */
  public void setAccountList(List<Account> accountList) {
    if (accountList == null) {
      this.accountList = new ArrayList<>();
    } else {
      this.accountList = accountList;
    }
  }

  /**
   * Thêm tài khoản cho khách hàng.
   *
   * @param account tài khoản cần thêm
   */
  public void addAccount(Account account) {
    if (account == null) {
      return;
    }
    if (!accountList.contains(account)) {
      accountList.add(account);
    }
  }

  /**
   * Xóa tài khoản khỏi khách hàng.
   *
   * @param account tài khoản cần xóa
   */
  public void removeAccount(Account account) {
    if (account == null) {
      return;
    }
    accountList.remove(account);
  }

  /**
   * Lấy thông tin khách hàng dưới dạng chuỗi văn bản.
   *
   * @return chuỗi chứa thông tin khách hàng
   */
  public String getCustomerInfo() {
    return "Số CMND: " + idNumber + ". Họ tên: " + fullName + ".";
  }
}
