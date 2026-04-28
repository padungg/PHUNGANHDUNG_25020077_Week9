package com.banksystem;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lớp đại diện cho ngân hàng, quản lý danh sách khách hàng.
 */
public class Bank {
  private static final Logger logger = LoggerFactory.getLogger(Bank.class);
  private List<Customer> customerList;

  /**
   * Khởi tạo ngân hàng với danh sách khách hàng rỗng.
   */
  public Bank() {
    this.customerList = new ArrayList<>();
  }

  public List<Customer> getCustomerList() {
    return customerList;
  }

  /**
   * Đặt danh sách khách hàng cho ngân hàng.
   *
   * @param customerList danh sách khách hàng cần đặt
   */
  public void setCustomerList(List<Customer> customerList) {
    if (customerList == null) {
      this.customerList = new ArrayList<>();
    } else {
      this.customerList = customerList;
    }
  }

  /**
   * Đọc danh sách khách hàng và tài khoản từ InputStream.
   *
   * @param inputStream luồng đầu vào chứa dữ liệu khách hàng
   */
  public void readCustomerList(InputStream inputStream) {
    logger.debug("Bắt đầu đọc dữ liệu khách hàng từ InputStream...");
    if (inputStream == null) {
      logger.warn("InputStream đầu vào bị null, bỏ qua việc đọc dữ liệu.");
      return;
    }

    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
      String line;
      Customer currentCustomer = null;
      while ((line = reader.readLine()) != null) {
        line = line.trim();
        if (line.isEmpty()) {
          continue;
        }

        currentCustomer = parseLineAndAddCustomer(line, currentCustomer);
      }
    } catch (Exception e) {
      logger.error("Lỗi trong quá trình đọc danh sách khách hàng: {}", e.getMessage(), e);
    }
  }

  private Customer parseLineAndAddCustomer(String line, Customer currentCustomer) {
    int lastSpaceIndex = line.lastIndexOf(' ');
    if (lastSpaceIndex <= 0) {
      return currentCustomer;
    }

    String token = line.substring(lastSpaceIndex + 1).trim();
    if (token.matches("\\d{9}")) {
      String name = line.substring(0, lastSpaceIndex).trim();
      Customer newCustomer = new Customer(Long.parseLong(token), name);
      customerList.add(newCustomer);
      logger.info("Đã thêm khách hàng mới: {} với CMND: {}", name, token);
      return newCustomer;
    }

    if (currentCustomer != null) {
      parseAccountForCustomer(line, currentCustomer);
    }
    return currentCustomer;
  }

  private void parseAccountForCustomer(String line, Customer currentCustomer) {
    String[] parts = line.split("\\s+");
    if (parts.length < 3) {
      return;
    }

    try {
      long accountNumber = Long.parseLong(parts[0]);
      double balance = Double.parseDouble(parts[2]);
      String accountType = parts[1];

      if (Account.CHECKING_TYPE.equals(accountType)) {
        currentCustomer.addAccount(new CheckingAccount(accountNumber, balance));
        logger.debug("Thêm tài khoản vãng lai {} cho khách hàng", accountNumber);
      } else if (Account.SAVINGS_TYPE.equals(accountType)) {
        currentCustomer.addAccount(new SavingsAccount(accountNumber, balance));
        logger.debug("Thêm tài khoản tiết kiệm {} cho khách hàng", accountNumber);
      } else {
        logger.warn("Loại tài khoản không hợp lệ: {}", accountType);
      }
    } catch (NumberFormatException e) {
      logger.error("Lỗi định dạng số khi đọc tài khoản: {}", line, e);
    }
  }

  /**
   * Lấy thông tin tất cả khách hàng sắp xếp theo số CMND (ID).
   *
   * @return chuỗi chứa thông tin khách hàng được sắp xếp
   */
  public String getCustomersInfoByIdOrder() {
    logger.debug("Bắt đầu lấy thông tin khách hàng sắp xếp theo ID.");
    List<Customer> copyList = new ArrayList<>(customerList);
    copyList.sort(Comparator.comparingLong(Customer::getIdNumber));

    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < copyList.size(); i++) {
      builder.append(copyList.get(i).getCustomerInfo());
      if (i < copyList.size() - 1) {
        builder.append("\n");
      }
    }
    return builder.toString();
  }

  /**
   * Lấy thông tin tất cả khách hàng sắp xếp theo tên, sau đó theo ID.
   *
   * @return chuỗi chứa thông tin khách hàng được sắp xếp
   */
  public String getCustomersInfoByNameOrder() {
    logger.debug("Bắt đầu lấy thông tin khách hàng sắp xếp theo Tên.");
    List<Customer> copyList = new ArrayList<>(customerList);
    copyList.sort((c1, c2) -> {
      int nameCompare = c1.getFullName().compareTo(c2.getFullName());
      if (nameCompare != 0) {
        return nameCompare;
      }
      return Long.compare(c1.getIdNumber(), c2.getIdNumber());
    });

    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < copyList.size(); i++) {
      builder.append(copyList.get(i).getCustomerInfo());
      if (i < copyList.size() - 1) {
        builder.append("\n");
      }
    }
    return builder.toString();
  }
}