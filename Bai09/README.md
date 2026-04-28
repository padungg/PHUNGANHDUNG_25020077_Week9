# Bài 9: Triển khai Logging chuyên nghiệp

## Phần 1: Lý Thuyết Trọng Tâm

### 1. Tại sao phải từ bỏ `System.out.println()`?
Trong môi trường chuyên nghiệp, việc dùng `System.out.println()` bị coi là "nghiệp dư" vì các lý do sau:
- **Hiệu suất cực kém:** Việc gọi hàm in ra màn hình làm block tiến trình xử lý chính (tính đồng bộ).
- **Không thể bật/tắt linh hoạt:** Khi lên môi trường Production, bạn không thể chọn tắt bớt các dòng in ra không cần thiết. Nó sẽ làm tràn bộ nhớ log.
- **Thiếu thông tin truy vết:** Hàm `println` không cho biết dòng chữ này sinh ra vào thời gian nào, ở file code nào, thuộc mức độ nghiêm trọng ra sao (INFO, ERROR, DEBUG).

### 2. Sức mạnh của SLF4J & Logback
- **SLF4J (Simple Logging Facade for Java):** Là một giao diện (Facade) cung cấp các hàm log chuẩn mực để dùng chung cho mọi thư viện.
- **Logback:** Là "bộ máy thực thi" (Implementation) đằng sau SLF4J. Nó cung cấp tốc độ ghi file cực nhanh và các tính năng như `FileAppender` (ghi ra file cứng) hay tự động xoay vòng file log (RollingFileAppender) để tránh đầy ổ cứng.

### 3. Parameterized Logging (Dấu giữ chỗ `{}`)
Thay vì cộng chuỗi tốn kém bộ nhớ: 
`logger.info("Số tiền: " + amount + " VND");`
Nên dùng dấu giữ chỗ: 
`logger.info("Số tiền: {} VND", amount);`
Cách viết này tối ưu hiệu suất vì Java chỉ thực hiện việc ghép chuỗi khi nó thực sự cần in dòng log đó ra.

---

## Phần 2: Hướng dẫn thực hành (Thao tác lấy điểm)

Tôi đã thiết lập sẵn toàn bộ code chuẩn mực cho thư mục `Bai09`. Trong đó:
- `pom.xml` đã tích hợp đủ thư viện SLF4J và Logback.
- `logback.xml` đã cấu hình **ConsoleAppender** (In ra màn hình) và **FileAppender** (Ghi ra file cứng tại `logs/application.log`).
- `PaymentProcessor.java` đã được tối ưu: Xóa toàn bộ `System.out.println`, dùng `logger.info`, `logger.error` và cú pháp `{}`.
- `PaymentProcessorTest.java` đã có 2 bài test kích hoạt việc ghi log.

### Bước 1: Chạy Test để sinh Log
1. Ở lề bên phải của màn hình IntelliJ, bấm mở tab **Maven**.
2. Bấm dấu **`+`** (Add Maven Projects) ở góc trên và chọn file `pom.xml` nằm trong thư mục `Bai09` -> Bấm OK.
3. Mở rộng nhánh **`Bai09`** -> nhánh **`Lifecycle`**.
4. Nhấp đúp chuột (Double-click) vào mục **`test`**.

### Bước 2: Quan sát Console và Chụp ảnh
Sau khi chạy xong, nhìn xuống cửa sổ Console ở phía dưới màn hình, bạn sẽ thấy các dòng log được định dạng cực chuẩn có chứa Ngày giờ, Mức độ (INFO/ERROR) và tên Class.
📸 **Hãy chụp màn hình đoạn log đỏ/trắng xen kẽ đẹp mắt này.**

### Bước 3: Quan sát tệp tin vật lý (FileAppender)
Nhìn vào danh sách cây thư mục bên trái (Project Explorer), bạn sẽ thấy bên trong thư mục `Bai09` vừa tự động sinh ra một thư mục con tên là **`logs`**, bên trong chứa file **`application.log`**.
1. Nhấp đúp mở file `application.log` ra.
2. Bạn sẽ thấy các dòng log đã được ghi chép vĩnh viễn vào ổ cứng để lưu trữ! 
📸 **Hãy chụp màn hình file này và nội dung bên trong nó để nộp vào Báo Cáo chứng minh bạn đã hoàn thành Bài 9.**
