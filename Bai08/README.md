# Bài 8: Đóng gói sản phẩm thực thi (Executable JAR)

## Phần 1: Thực hành đóng gói ứng dụng

Dự án này đã được cấu hình sẵn để đóng gói thành một file thực thi độc lập (có thể mang sang máy tính khác chạy mà không cần mở phần mềm IntelliJ IDEA).

### Bước 1: Cấu hình `maven-jar-plugin`
Trong file `pom.xml`, tôi đã thêm cấu hình cho plugin `maven-jar-plugin`. Điểm quan trọng nhất ở đây là thẻ `<mainClass>com.packaging.App</mainClass>`.
Thẻ này sẽ ghi thông tin vào file `META-INF/MANIFEST.MF` nằm bên trong lõi file nén `.jar`. Nhờ đó, Java sẽ biết hàm `main()` nằm ở class nào để tự động chạy khi người dùng gõ lệnh thực thi.

### Bước 2: Đóng gói (Package)
Bạn hãy mở Terminal trong IntelliJ, gõ lệnh chuyển vào thư mục Bài 8 và chạy lệnh đóng gói:
```bash
cd Bai08
mvn clean package
```
Lệnh này sẽ biên dịch mã nguồn và nén toàn bộ thành một file `.jar` trong thư mục `target`.

### Bước 3: Chạy ứng dụng (Xác minh)
Sau khi đóng gói xong, bạn hãy chạy thẳng phần mềm này bằng dòng lệnh Terminal (không bấm nút Run của IDE):
```bash
java -jar target/Bai08-1.0-SNAPSHOT.jar
```
📸 **Yêu cầu nộp báo cáo:** Hãy chụp màn hình kết quả dòng lệnh này khi nó in ra dòng chữ *"CHÚC MỪNG! PHẦN MỀM ĐÃ ĐƯỢC ĐÓNG GÓI THÀNH CÔNG!"* để nộp cho giảng viên.

---

## Phần 2: Giải thích lý thuyết (Dùng để đưa vào Báo Cáo)

Dưới đây là phần giải thích chi tiết cho câu hỏi lý thuyết của đề bài:

### 1. Thư mục `target` trong Maven là gì?
Thư mục `target` là thư mục tạm thời do Maven tự động tạo ra trong quá trình build dự án. Nó được dùng để chứa **toàn bộ các sản phẩm đầu ra (output)**, bao gồm:
- `.class`: Các file bytecode sau khi biên dịch từ mã nguồn gốc `.java`.
- Báo cáo kiểm thử (Surefire, JaCoCo).
- Sản phẩm đóng gói cuối cùng (file `.jar` hoặc `.war`).

**Đặc điểm:** Thư mục này mang tính chất "dùng xong bỏ", không bao giờ được commit đưa lên Git (thường bị đưa vào file `.gitignore`). Khi bạn chạy lệnh `mvn clean`, Maven sẽ xoá sạch thư mục này để đảm bảo bản build mới hoàn toàn sạch sẽ, không bị lẫn lộn lỗi với phiên bản cũ.

### 2. Pha `package` trong vòng đời Maven là gì?
Maven có một vòng đời chuẩn (Lifecycle) gồm nhiều pha (phase) thực thi nối tiếp nhau: `validate` -> `compile` -> `test` -> `package` -> `verify` -> `install` -> `deploy`.

Pha **`package`** (Đóng gói) đảm nhiệm vai trò vô cùng quan trọng:
1. Nó kế thừa quá trình trước: Chạy `package` sẽ tự động kích hoạt `compile` để dịch code và `test` để kiểm tra lỗi.
2. Đóng gói: Lấy toàn bộ mã nguồn đã biên dịch (nằm trong thư mục `target/classes`) nén lại thành một định dạng có thể phân phối được, phổ biến nhất là file `.jar` (Java ARchive).
3. Đính kèm siêu dữ liệu (Metadata): Chèn file `MANIFEST.MF` để định nghĩa đây là một phần mềm có thể chạy độc lập.
