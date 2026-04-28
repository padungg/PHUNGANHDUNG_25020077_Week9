# Bài 5: Test Coverage & Quality Enforcement với JaCoCo

## Phần 1: Lý Thuyết Trọng Tâm

### 1. Code Coverage (Độ bao phủ mã) là gì?
Viết Unit Test là tốt, nhưng làm sao bạn biết mình đã test đủ chưa? Có đoạn code nào bị bỏ sót không?
**Code Coverage** là chỉ số đo lường tỷ lệ phần trăm các dòng code, các nhánh điều kiện (if/else) thực sự được thực thi khi chạy các bài Unit Test.
Chỉ số này càng cao (thường tiêu chuẩn là 80%), ứng dụng càng ít có khả năng chứa các lỗi ngầm.

### 2. JaCoCo (Java Code Coverage)
JaCoCo là thư viện phổ biến nhất trong thế giới Java để tính toán Code Coverage. 
Nó được tích hợp vào Maven thông qua `jacoco-maven-plugin`. Plugin này sẽ:
1. Lắng nghe quá trình test để đếm số dòng code được chạy.
2. Sinh ra một bản báo cáo chi tiết dưới dạng HTML (ở thư mục `target/site/jacoco/index.html`).
3. (Nâng cao) Tự động đánh hỏng (Fail) bản build nếu chỉ số Coverage thấp hơn mức mà lập trình viên đề ra.

---

## Phần 2: Hướng dẫn thực hành

### Bước 1: Khởi tạo code và test
Tôi đã tạo sẵn cho bạn một class `Calculator` có 2 hàm `add` và `subtract`. 
Đồng thời, có một file test `CalculatorTest` kiểm tra cả 2 hàm này (Đạt độ bao phủ 100%).

### Bước 2: Cấu hình `pom.xml` để tích hợp JaCoCo
Tôi đã thêm `jacoco-maven-plugin` vào `pom.xml` với 3 hành động chính (executions):
1. `prepare-agent`: Gắn công cụ theo dõi vào tiến trình chạy Test.
2. `report`: Trích xuất báo cáo ra HTML.
3. `check`: Thiết lập luật bắt buộc (Strict Rule) là `COVEREDRATIO` phải đạt tối thiểu `0.80` (Tức 80%). Nếu dưới 80%, lệnh `mvn verify` sẽ bị lỗi.

### Bước 3: Cấu hình GitHub Actions
Tôi đã tạo file `.github/workflows/bai05.yml` thực thi lệnh `mvn -B verify` để vừa test vừa check độ bao phủ. 
Đồng thời, thêm cấu hình `actions/upload-artifact@v4` để đóng gói thư mục chứa báo cáo HTML đẩy lên GitHub để bạn xem lại.

---

## Phần 3: Việc bạn cần làm để lấy điểm

### Thao tác 1: Chạy test Xanh (100% Coverage)
1. Mở Terminal và gõ:
   ```bash
   git add .
   git commit -m "Them Bai 05 voi JaCoCo coverage"
   git push
   ```
2. Lên GitHub kiểm tra tiến trình `Bai 05 - JaCoCo Coverage`, nó sẽ **Xanh lá**.
3. Bấm vào tiến trình đó, cuộn xuống dưới cùng, bạn sẽ tải được file `jacoco-report.zip`. Giải nén ra, bấm vào `index.html` để xem báo cáo (nó sẽ hiển thị 100%).

### Thao tác 2: Cố tình hạ điểm Coverage để test lỗi
1. Bạn mở file `Bai05/src/test/java/com/testing/CalculatorTest.java`.
2. Bạn **xóa hoặc comment (//)** toàn bộ đoạn test của hàm `testSubtract` đi. Lúc này, test chỉ cover được 50% số hàm của class `Calculator` (không đạt chuẩn 80%).
3. Lưu lại và đẩy code lỗi lên GitHub:
   ```bash
   git add .
   git commit -m "Co tinh comment test de ha coverage xuong duoi 80"
   git push
   ```
4. Lên GitHub xem lại tab Actions. Lần này tiến trình sẽ bị **Lỗi đỏ (Failed)**.
5. Mở log ra xem, bạn sẽ thấy lỗi: 
   `[WARNING] Rule violated for bundle Bai05: instructions covered ratio is 0.50, but expected minimum is 0.80`
   *(Nghĩa là: Độ bao phủ chỉ đạt 50%, trong khi yêu cầu tối thiểu là 80%).*
6. 📸 **Hãy chụp màn hình log bị đỏ này lại làm báo cáo!** Cuối cùng, mở comment ra để code quay lại 100% và push lên cho Xanh.
