# Bài 3: CI/CD Automation với Maven, SLF4J, JUnit 5 và GitHub Actions

Tài liệu này cung cấp nền tảng lý thuyết và hướng dẫn từng bước để bạn có thể tự tay thực hiện việc cấu trúc một dự án Maven, tích hợp Logging, Unit Test và xây dựng quy trình tự động hóa (CI) bằng GitHub Actions.

---

## Phần 1: Lý Thuyết Trọng Tâm

### 1. CI/CD là gì?
- **CI (Continuous Integration - Tích hợp liên tục):** Là phương pháp phát triển phần mềm mà các lập trình viên thường xuyên (hàng ngày hoặc thậm chí vài giờ một lần) tích hợp code mới của họ vào một nhánh chính (như `main`). Mỗi lần tích hợp (push code) đều được tự động xác minh bằng cách tự động build (biên dịch mã) và chạy các test tự động (Automated Testing) để phát hiện sớm các lỗi tích hợp.
- **CD (Continuous Delivery/Deployment - Phân phối/Triển khai liên tục):** Là bước tiếp theo của CI, nơi mà code đã vượt qua bài test sẽ tự động được đóng gói và sẵn sàng để triển khai lên môi trường kiểm thử (staging) hoặc môi trường thực tế (production).

### 2. GitHub Actions
GitHub Actions là một nền tảng CI/CD được tích hợp trực tiếp vào GitHub, giúp tự động hóa quy trình xây dựng, thử nghiệm và triển khai.
- **Workflow:** Là một quy trình tự động hóa hoàn chỉnh, được cấu hình qua một file YAML (`.yml`) đặt trong thư mục `.github/workflows/`.
- **Event (Sự kiện):** Là trigger để kích hoạt workflow, ví dụ như `push` code, tạo `pull_request`.
- **Job:** Một workflow gồm một hoặc nhiều job. Các job mặc định chạy song song.
- **Step:** Mỗi job bao gồm nhiều bước chạy tuần tự. Một step có thể là chạy một lệnh shell hoặc sử dụng một "Action" đã được đóng gói sẵn (ví dụ: checkout code, cài đặt Java).

### 3. Vòng đời Maven (Maven Build Lifecycle)
Maven chia quy trình build thành các "phase" (pha) nối tiếp nhau. Chạy một pha sẽ tự động chạy tất cả các pha trước đó:
1. `validate`: Xác minh tính hợp lệ của project.
2. `compile`: Biên dịch mã nguồn Java.
3. `test`: Chạy Unit Test (thường qua thư viện `maven-surefire-plugin`).
4. `package`: Đóng gói mã đã biên dịch thành `.jar` hoặc `.war`.
5. `install`: Đưa gói `.jar` vào kho chứa (local repository) trên máy của bạn.

Trong bài tập này, lệnh `mvn clean package` sẽ dọn dẹp thư mục `target/` cũ, sau đó dịch, chạy test, và đóng gói ra file JAR.

### 4. Logging với SLF4J & Logback thay vì `System.out.println()`
- Khác với `System.out.println()` luôn in tất cả mọi thứ ra màn hình (không thể tắt, không phân loại), hệ thống **Logging** chuyên nghiệp cho phép bạn thiết lập các mức độ (Level) như: `TRACE`, `DEBUG`, `INFO`, `WARN`, `ERROR`.
- **SLF4J (Simple Logging Facade for Java)**: Cung cấp API (Giao diện) để viết code log.
- **Logback**: Là bộ thư viện thực thi ngầm ở bên dưới (Implementation) lo việc xuất log ra console hoặc ghi ra file tùy thuộc vào file cấu hình `logback.xml`.

---

## Phần 2: Hướng dẫn chi tiết cách làm bài

### Bước 1: Khởi tạo và cấu hình dự án Maven (`pom.xml`)
Để chuẩn hóa dự án, việc đầu tiên là khai báo các dependencies và plugin trong file `pom.xml`:
1. Mở `Bai03/pom.xml`.
2. Thêm thẻ `<dependencies>` và khai báo 3 thư viện chính:
   - `slf4j-api`
   - `logback-classic`
   - `junit-jupiter-api` và `junit-jupiter-engine` (dành cho Unit Test).
3. Thêm thẻ `<build><plugins>` để khai báo Maven compiler (chỉ định phiên bản Java 17), Maven Surefire (để chạy Unit test) và Maven Jar Plugin.

### Bước 2: Xây dựng hệ thống và tích hợp Logging
1. Tạo class thực thể `Order.java` lưu trữ thông tin đơn hàng (ID, Amount, isPaid).
2. Tạo class `OrderProcessor.java` chứa nghiệp vụ xử lý đơn hàng:
   - Khởi tạo biến logger: 
     `private static final Logger logger = LoggerFactory.getLogger(OrderProcessor.class);`
   - Sử dụng các hàm như `logger.info(...)`, `logger.error(...)`, `logger.warn(...)` để đánh dấu tiến trình thực thi code thay vì `println()`.
3. Tạo file `src/main/resources/logback.xml` để cấu hình hiển thị log ra màn hình console và cấu hình mức độ log cơ bản là `DEBUG`.

### Bước 3: Viết Unit Test bằng JUnit 5
1. Trong thư mục `src/test/java/...`, tạo class `OrderProcessorTest.java`.
2. Sử dụng Annotation `@BeforeEach` để khởi tạo `OrderProcessor` mới trước mỗi test case.
3. Sử dụng `@Test` cho các hàm kiểm tra kịch bản khác nhau:
   - Kịch bản đơn hàng hợp lệ (`assertTrue`).
   - Kịch bản số tiền bị âm (`assertFalse`).
   - Kịch bản truyền vào đơn hàng `null` (`assertThrows` IllegalArgumentException).

### Bước 4: Viết file Workflow CI của GitHub Actions
1. Tại thư mục gốc (root) của toàn bộ Project (ngang hàng với các thư mục Bai01, Bai02...), bạn tạo cấu trúc thư mục: `.github/workflows/`.
2. Tạo file `ci.yml` với các phần cấu hình chính:
   - Khai báo tên và event:
     ```yaml
     on:
       push:
         branches: [ "main", "master" ]
     ```
   - Khai báo Jobs:
     ```yaml
     jobs:
       build:
         runs-on: ubuntu-latest
     ```
   - Khai báo các steps:
     - Kéo mã nguồn về (`actions/checkout`).
     - Cài đặt Java 17 (`actions/setup-java`).
     - Chạy lệnh Maven Build: `run: mvn -B clean package` với thư mục làm việc (working-directory) trỏ vào `./Bai03`.
     - Tải file JAR tạo ra lên mục Artifact: sử dụng `actions/upload-artifact`.

### Bước 5: Hướng dẫn chi tiết cách chạy GitHub Actions và Kiểm thử lỗi CI

Mục đích của CI (Continuous Integration) là tự động chạy toàn bộ bài Test (Unit Test) mỗi khi có người đẩy (push) code mới lên hệ thống. Nếu có bất kỳ đoạn code nào vi phạm logic, CI sẽ "báo đỏ" để ngăn chặn code hỏng được tích hợp. Để chứng minh điều này hoạt động, chúng ta sẽ thao tác như sau:

#### 5.1. Xem tiến trình CI chạy thành công (Xanh lá)
Mỗi khi bạn gõ lệnh `git push -u origin main` (hoặc `git push`), GitHub Actions sẽ tự động kích hoạt tiến trình dựa trên file cấu hình `.github/workflows/ci.yml`.
1. Mở trang web GitHub chứa Repository của bạn.
2. Nhấn vào tab **Actions** ở menu ngang trên cùng.
3. Bạn sẽ thấy một tiến trình (Workflow run) mang tên commit của bạn đang chạy. Khi mã nguồn hoàn hảo, tiến trình sẽ hoàn tất với dấu check màu **Xanh lá (Passed)**. 
4. Bấm vào tiến trình đó, cuộn xuống dưới cùng, bạn sẽ thấy file JAR đã được hệ thống tự động đóng gói (`Bai03-jar.zip`) nằm ở mục Artifacts sẵn sàng tải về.

#### 5.2. Cố tình gây lỗi để kiểm chứng hệ thống phòng vệ CI (Báo Đỏ)
Để hiểu rõ vai trò "người gác cổng" của CI, ta sẽ cố tình phá hỏng logic của chương trình:
1. **Sửa code sai logic:** Mở file `OrderProcessor.java`. Tìm đến dòng mô phỏng thanh toán thành công `order.setPaid(true);` và đổi nó thành `order.setPaid(false);`.
   - 💡 **Giải thích tại sao làm vậy:** Trong file test `OrderProcessorTest.java`, chúng ta có viết một hàm kiểm tra: `assertTrue(order.isPaid())`. Nghĩa là bài test **kỳ vọng** đơn hàng sau khi xử lý phải chuyển sang trạng thái "đã thanh toán" (`true`). Khi ta đổi code nghiệp vụ thành `false`, kết quả trả về sẽ không khớp với kỳ vọng của bài Test, dẫn đến Test thất bại.
2. **Đẩy đoạn code bị lỗi lên mạng:**
   Mở Terminal và gõ lần lượt 3 lệnh:
   - `git add .`
   - `git commit -m "Co tinh tao loi de kiem thu CI"`
   - `git push`
3. **Đọc và phân tích lỗi trên GitHub Actions:**
   - Lên lại tab **Actions** trên trình duyệt. Bạn sẽ thấy một tiến trình mới vừa chạy và lập tức bị đánh dấu **X màu đỏ (Failed)**.
   - Bấm vào tên tiến trình đó, mở rộng phần log của bước **Build with Maven (Test and Package)**.
   - Khi đọc log, bạn sẽ tìm thấy lỗi báo: `expected: <true> but was: <false>`, kèm theo thông báo file bắt được lỗi chính là `OrderProcessorTest.java`. Điều này cực kỳ quan trọng vì nó chứng minh hệ thống CI đã tự động "tóm gọn" được lỗi do lập trình viên lỡ tay gây ra trước khi ứng dụng được mang đi triển khai!
4. **Sửa lại cho đúng (Fix bug):**
   - Quay về IDE, sửa `false` lại thành `true`.
   - Chạy lại các lệnh `git add`, `git commit -m "Sua loi test"`, `git push` để đẩy bản vá lỗi (hotfix) lên.
   - Lên GitHub xem lại, tiến trình bảo vệ sẽ đánh giá code đã an toàn và hiện Xanh lá trở lại! 🎉

---
*Bằng cách làm theo luồng bài tập này, bạn không chỉ code xong chức năng mà còn áp dụng đúng chuẩn làm việc của quy trình phát triển phần mềm chuyên nghiệp hiện nay (Code -> Tự động Test -> Tự động Build -> Cảnh báo lỗi).*
