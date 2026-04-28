# Bài 10: The Broken Pipeline (Sửa lỗi theo chuỗi)

Dự án này mô phỏng quá trình thực tế khi bạn tiếp nhận một hệ thống cũ chứa đầy lỗi. Nhiệm vụ của bạn là đẩy nó lên mạng, phân tích log đỏ, sửa từng lỗi một cho đến khi Xanh. Không được sửa "mò" mà phải sửa dựa trên chứng cứ từ Log!

---

## Phần 1: Quy trình thực hành tìm và sửa lỗi (Làm theo thứ tự)

### Bước 1: Khởi động - Đẩy "code rác" lên mạng
Khoan vội sửa gì cả! Hãy cứ để nguyên toàn bộ thư mục `Bai10` đang chứa các lỗi cố ý này và đẩy lên GitHub bằng lệnh:
```bash
git add .
git commit -m "Thử nghiệm Bài 10 với code chứa đầy lỗi"
git push
```

### Bước 2: Đi săn Lỗi số 1 (Lỗi Workflow)
1. Lên trang GitHub, mở tab **Actions**, bấm vào tiến trình vừa chạy đang bị màu Đỏ. Nhấp vào Job **`build`**, mở rộng bước bị lỗi chéo đỏ: **`Build with Maven`**.
2. **Đọc Log:** Cuộn tìm dòng có chữ `[ERROR]`, bạn sẽ thấy:
   > `[ERROR] The goal you specified requires a project to execute but there is no POM in this directory. Please verify you invoked Maven from the correct directory.`
3. **Phân tích:** Máy tính báo không tìm thấy file `pom.xml` nào ở thư mục `Bai10` cả. Tại sao? Vì ta đã quên dặn GitHub kéo code từ kho lưu trữ về máy ảo (thiếu hành động `checkout`).
4. **Sửa lỗi & Đẩy lên:** Mở file `.github/workflows/bai10.yml`, thêm `- uses: actions/checkout@v4` vào trước bước `Set up JDK 17`. Lưu lại, đẩy lên GitHub.

### Bước 3: Đi săn Lỗi số 2 (Lỗi Thư Viện)
1. Chờ tiến trình mới chạy và... nó vẫn bị Đỏ! Lại vào xem log của bước **`Build with Maven`**.
2. **Đọc Log:** Lần này lỗi đã khác, bạn tìm dòng `[ERROR]` sẽ thấy:
   > `[ERROR] Failed to execute goal ... Could not resolve dependencies for project com.lab:shipping-app ... ch.qos.logback:logback-classic:jar:9.9.9 was not found in https://repo.maven.apache.org...`
3. **Phân tích:** Không thể tải (resolve) thư viện `logback-classic` phiên bản `9.9.9` từ mạng về. Phiên bản này là phiên bản "ma" do ai đó gõ bừa vào không hề tồn tại.
4. **Sửa lỗi & Đẩy lên:** Mở `Bai10/pom.xml`, sửa dòng `<version>9.9.9</version>` thành `<version>1.4.14</version>`. Lưu lại, đẩy lên mạng.

### Bước 4: Đi săn Lỗi số 3 (Lỗi Cú Pháp Java)
1. Hệ thống tiếp tục chạy và... vẫn Đỏ tiếp! Đừng nản, vì đây là lỗi cuối cùng.
2. **Đọc Log:** Lần này thông báo trên Terminal sẽ là:
   > `[ERROR] COMPILATION ERROR :`
   > `[ERROR] /home/runner/.../Bai10/src/main/java/com/lab/ShippingCalculator.java:[6,48] unclosed string literal`
3. **Phân tích:** Log chỉ đích danh file `ShippingCalculator.java` ở **dòng số 6** bị lỗi `unclosed string literal` (Lỗi cú pháp: Chuỗi viết ra nhưng quên không đóng ngoặc kép do bị gõ phím Enter bẻ dòng tùy tiện).
4. **Sửa lỗi & Đẩy lên:** Mở file đó ra, gom chuỗi `"Weight must be positive"` lên cho nằm gọn trong cùng 1 dòng. Lưu lại, đẩy code lên mạng.

**🎉 KẾT QUẢ:** Lần này bạn lên xem, toàn bộ Pipeline sẽ báo **Xanh Lá cây** hoàn hảo!

---

## Phần 2: THỰC HÀNH TẠO LỖI THỨ 4 (Lấy điểm tuyệt đối)

Sau khi bạn sửa xong 3 lỗi trên và thấy **Pipeline Xanh lá**, hãy tạo thêm lỗi thứ 4 này để nộp báo cáo lấy điểm tuyệt đối:

### Lỗi 4: Lỗi NullPointerException (Lỗi sập hệ thống do dữ liệu rỗng)
- **Cách tạo lỗi:** Mở file `Bai10/src/test/java/com/lab/ShippingCalculatorTest.java`, thêm đoạn test sau vào cuối file (trước dấu `}` cuối cùng):
  ```java
  @Test
  void testNullType() {
      calc.calculate(5, null);
  }
  ```
- **Push code lên GitHub:** Pipeline sẽ chuyển sang **màu Đỏ**. Mở log ra sẽ thấy dòng chữ sập hệ thống cực kỳ quen thuộc: `java.lang.NullPointerException`. 📸 *Chụp màn hình cái log đó lại.*
- **Giải thích kỹ thuật:** Trong file `ShippingCalculator.java` có dòng `if (type.equals("EXPRESS"))`. Hàm `.equals()` được gọi từ biến `type`. Do biến `type` mang giá trị `null` (do ta cố tình truyền vào ở file test), việc gọi một hàm từ `null` sẽ gây sập hệ thống ngay lập tức.
- **Cách sửa lỗi:** Sửa lại code trong `ShippingCalculator.java` bằng cách đảo ngược chuỗi để so sánh (Kỹ thuật Yoda Condition):
  ```java
  if ("EXPRESS".equals(type)) return weight * 5000 + 20000;
  if ("STANDARD".equals(type)) return weight * 3000;
  ```
  *(Giải thích: Khi ta đổi lên viết chuỗi cứng `"EXPRESS"` trước, vì chuỗi này luôn có thật, nên khi gọi hàm `.equals(null)` nó sẽ chỉ nhẹ nhàng trả về `false` một cách an toàn mà không làm sập chương trình).*
- **Lưu và Push lại:** Lúc này Pipeline sẽ Xanh trở lại!
