# Bài 02: Code Quality - Checkstyle & Observability

> **Môn:** Lập trình nâng cao
> **Sinh viên:** Phùng Anh Dũng – MSSV: 25020077
> **Tuần:** 9

---

## Mục lục

1. [Đề bài](#1-đề-bài)
2. [Lý thuyết nền tảng](#2-lý-thuyết-nền-tảng)
   - 2.1 [Code Quality & Static Analysis](#21-code-quality--static-analysis)
   - 2.2 [Maven Checkstyle Plugin](#22-maven-checkstyle-plugin)
   - 2.3 [Google Java Style Guide](#23-google-java-style-guide)
   - 2.4 [Observability & Logging (SLF4J + Logback)](#24-observability--logging-slf4j--logback)
3. [Hướng dẫn thực hiện (Refactoring Guide)](#3-hướng-dẫn-thực-hiện-refactoring-guide)
   - Bước 1: Cấu hình Maven Checkstyle & SLF4J
   - Bước 2: Tái cấu trúc theo Google Style
   - Bước 3: Tích hợp Logging
4. [Giải thích chiến lược Logging đã chọn](#4-giải-thích-chiến-lược-logging-đã-chọn)
5. [Hướng dẫn chạy dự án](#5-hướng-dẫn-chạy-dự-án)

---

## 1. Đề bài

Một dự án `BankSystem` cũ chứa nhiều lỗi thiết kế và phong cách code không chuẩn. Mục tiêu là áp dụng các tiêu chuẩn chất lượng code chuyên nghiệp và khả năng quan sát (observability) bằng cách thực hiện các bước sau:

1.  **Tích hợp Maven Checkstyle Plugin** vào quy trình build của dự án.
2.  **Chọn và áp dụng một tiêu chuẩn code** (sử dụng Google Java Style) để xác định tất cả các vi phạm về định dạng và đặt tên.
3.  **Tái cấu trúc (Refactor)** mã nguồn để giải quyết tất cả các lỗi được tìm thấy và vượt qua goal `checkstyle:check` thành công.
4.  **Theo dõi trạng thái hoạt động** của hệ thống thông qua logging (sử dụng SLF4J hoặc Logback). Giải thích lý do chọn các cấp độ log (logging levels) và các điểm dữ liệu chọn để ghi lại.

---

## 2. Lý thuyết nền tảng

### 2.1 Code Quality & Static Analysis
**Code Quality** (Chất lượng mã) đánh giá mức độ mã nguồn dễ đọc, dễ bảo trì, an toàn và ít lỗi. Để đảm bảo điều này, các dự án phần mềm sử dụng **Static Analysis** (Phân tích tĩnh) - một phương pháp tự động kiểm tra source code (mà không cần thực thi) để phát hiện vi phạm quy tắc (Code smells, bugs, formatting violations).

### 2.2 Maven Checkstyle Plugin
**Checkstyle** là một công cụ giúp các lập trình viên tuân thủ tiêu chuẩn code (coding standard) thông qua việc quét mã nguồn Java. Khi được tích hợp dưới dạng Maven plugin, nó trở thành một phần của quy trình CI/CD.

*   **Tích hợp trong `pom.xml`**:
    Sử dụng `maven-checkstyle-plugin` và thiết lập `configLocation` trỏ đến `google_checks.xml` (bộ quy tắc chuẩn của Google có sẵn trong thư viện plugin). Cấu hình `failsOnError` giúp chặn quá trình build nếu phát hiện mã nguồn vi phạm lỗi quy chuẩn.

### 2.3 Google Java Style Guide
Google quy định rất chặt chẽ về cách code Java. Một số quy tắc quan trọng áp dụng vào bài này:
1.  **Thụt lề (Indentation)**: Bắt buộc dùng 2 spaces (khoảng trắng), không được dùng Tabs.
2.  **Độ dài dòng (Line Length)**: Không được vượt quá 100 ký tự mỗi dòng. Nếu vượt quá, phải ngắt dòng (Line wrapping).
3.  **Đặt tên (Naming Convention)**:
    *   Hằng số (Constants): Dùng `UPPER_SNAKE_CASE` (Ví dụ: `CHECKING_TYPE`).
    *   Biến/Phương thức: Dùng `camelCase` (Ví dụ: `accountNumber`, `getTypeString()`). Không dùng tiền tố `_` hoặc tên quá ngắn.
4.  **Imports**: Không sử dụng Wildcard import (`import java.util.*`). Phải khai báo chi tiết từng class.
5.  **Braces `{}`**: Bắt buộc phải có `{}` cho cấu trúc `if, else, for, while` dù thân vòng lặp chỉ có 1 lệnh.
6.  **Javadoc**: Bắt buộc có Javadoc cho mọi class, phương thức public. Javadoc phải tuân thủ đúng định dạng với các tag `@param`, `@return` hợp lệ.

### 2.4 Observability & Logging (SLF4J + Logback)
**Observability** (Khả năng quan sát) đo lường mức độ hiểu rõ trạng thái bên trong của một hệ thống dựa trên đầu ra của nó (Logs, Metrics, Traces). Trong Java, **Logging** là yếu tố cốt lõi của Observability.

*   **SLF4J (Simple Logging Facade for Java)**: Là một giao diện (interface/facade). Nó cho phép viết code log độc lập với thư viện thực thi bên dưới.
*   **Logback**: Là công cụ (implementation) nằm dưới SLF4J, thực hiện nhiệm vụ in ra console hoặc ghi ra file một cách an toàn và tốc độ cao. (Thay thế cho cách dùng `System.out.println` lỗi thời và nguy hiểm trên môi trường multi-threading).

Các cấp độ log trong SLF4J theo mức độ nghiêm trọng:
**`TRACE` < `DEBUG` < `INFO` < `WARN` < `ERROR`**

---

## 3. Hướng dẫn thực hiện (Refactoring Guide)

Dưới đây là chi tiết các bước làm bài tập này:

### Bước 1: Cấu hình Maven
Mở `pom.xml` và khai báo:
*   Dependency cho logging: Thêm `logback-classic`.
*   Plugin Checkstyle: Cấu hình chạy tự động ở phase `validate`.

### Bước 2: Tái cấu trúc theo Google Style (Refactor code cũ)
Dựa vào các báo cáo từ lệnh `mvn checkstyle:check`, ta tiến hành sửa đổi lần lượt các lớp:
*   **Lớp `Account`**: 
    *   Xóa `import java.util.*` và thêm `import java.util.ArrayList; import java.util.List;`.
    *   Sửa hằng số `checking_type` -> `CHECKING_TYPE`.
    *   Sửa tên biến `_accNum` -> `accountNumber`, `B` -> `balance`, `list` -> `transactionList`.
    *   Thêm block `{}` cho câu lệnh `if (amount <= 0)`.
    *   Thêm Javadoc cho toàn bộ phương thức `deposit`, `withdraw`.
*   **Lớp `Transaction`**:
    *   Sửa `get_type_string` thành `getTypeString` để tuân thủ camelCase.
    *   Chuỗi `String.format()` nối dài bị lỗi quá 100 ký tự -> Chia cắt thành các biến riêng biệt và nối chuỗi bằng `StringBuilder` để tối ưu bộ nhớ.
*   **Lớp `Bank`**:
    *   Loại bỏ `System.out.println` và khởi tạo `Logger logger = LoggerFactory.getLogger(Bank.class)`.
    *   Sửa cấu trúc lồng nhau (nested-if) bằng cách return sớm (early return/continue) để code gọn gàng (đảm bảo độ dài không thụt lề quá sâu).
    *   Sử dụng Lambda thay thế cho Anonymous Class trong hàm sắp xếp danh sách khách hàng.
*   **Các lớp Exception**: Thêm Javadoc giải thích rõ nguyên nhân lỗi (Ví dụ: `InsufficientFundsException`).

### Bước 3: Tích hợp Logging
*   Khai báo `Logger` trong các class nghiệp vụ (`Account`, `SavingsAccount`, `CheckingAccount`, `Bank`, `Transaction`).
*   Chuyển đổi hoàn toàn các dòng lệnh bắt Exception `System.out.println(e.getMessage())` thành `logger.error("Lỗi giao dịch: {}", e.getMessage(), e);`

---

## 4. Giải thích chiến lược Logging đã chọn

Hệ thống được cài đặt 4 cấp độ log. Lý do lựa chọn cấp độ và điểm dữ liệu:

1.  **Mức độ `INFO` (Thông tin nghiệp vụ)**
    *   **Lý do**: Để theo dõi chu kỳ sống chính của hệ thống. Những log này được giữ lại trên Production để kiểm toán (audit).
    *   **Điểm dữ liệu**: Khi nạp/rút tiền thành công (log ra số tiền và số tài khoản). Khi khởi tạo và thêm khách hàng thành công.
2.  **Mức độ `DEBUG` (Gỡ lỗi hệ thống)**
    *   **Lý do**: Chứa các thông tin sâu về luồng thực thi nhằm hỗ trợ dev, nhưng có thể gây nhiễu nếu bật trên môi trường thật (Production).
    *   **Điểm dữ liệu**: Lúc ngân hàng bắt đầu đọc danh sách dữ liệu từ InputStream, các nhánh rẽ khi tìm thấy loại tài khoản tiết kiệm hay vãng lai.
3.  **Mức độ `WARN` (Cảnh báo)**
    *   **Lý do**: Có những rủi ro bất thường hoặc lỗi nhỏ nhưng hệ thống vẫn tự xoay sở và tiếp tục chạy được.
    *   **Điểm dữ liệu**: InputStream đầu vào truyền vào bằng `null`, hoặc hệ thống đọc thấy mã tài khoản không được định nghĩa trước.
4.  **Mức độ `ERROR` (Nghiêm trọng/Ngoại lệ)**
    *   **Lý do**: Bất kỳ khi nào một ngoại lệ (Exception) xảy ra làm đứt gãy luồng xử lý tự nhiên.
    *   **Điểm dữ liệu**: Ghi nhận toàn bộ `StackTrace` của lỗi I/O, lỗi sai format số, và các ngoại lệ về giao dịch thất bại như `InsufficientFundsException`. Điểm dữ liệu bao gồm số tài khoản gặp lỗi và thông báo chi tiết.

---

## 5. Hướng dẫn chạy dự án

### Kiểm tra Code Quality (Checkstyle)
Mở Terminal ở thư mục chứa project (`Bai02`) và gõ lệnh:
```bash
mvn checkstyle:check
```
*(Nếu console báo `BUILD SUCCESS` đồng nghĩa toàn bộ source code không vi phạm 1 lỗi nào theo quy chuẩn của Google Java Style Guide).*

### Build và chạy Test (bao gồm tự động sinh file phân tích)
Chạy lệnh biên dịch mã nguồn (sẽ tự động chạy checkstyle do gắn vào phase validate):
```bash
mvn clean compile
```

Để sinh báo cáo dạng HTML giao diện web (Checkstyle Report):
```bash
mvn site
```
*Kết quả phân tích có thể tìm thấy tại file `target/site/checkstyle.html`.*
