# Bài 6: CI/CD Pipeline Optimization & Caching

## Phần 1: Lý Thuyết Trọng Tâm

### 1. Tại sao cần Caching trong CI/CD?
Mỗi khi bạn đẩy code lên GitHub, GitHub Actions sẽ tạo ra một máy ảo (Runner) hoàn toàn mới, sạch sẽ và không có bất kỳ dữ liệu cũ nào để chạy tiến trình của bạn. 
Đối với dự án Java sử dụng Maven, điều này có nghĩa là mỗi lần chạy, Maven phải "hì hục" tải lại toàn bộ các thư viện (dependencies) từ kho lưu trữ xa xôi (Maven Central) về máy ảo. Việc này lặp đi lặp lại sẽ tốn rất nhiều thời gian chờ đợi và gây lãng phí băng thông mạng.

### 2. Dependency Caching hoạt động như thế nào?
**Caching (Bộ nhớ đệm)** giải quyết vấn đề này bằng cách lưu trữ lại thư mục chứa các thư viện đã tải (đối với Maven là thư mục `~/.m2/repository`) và nén nó lên hệ thống lưu trữ của GitHub sau khi lần chạy đầu tiên kết thúc.
Ở những lần chạy tiếp theo, thay vì tải lại từ đầu, GitHub Actions sẽ phát hiện ra file nén này và "giải nén" thẳng vào thư mục `.m2`. Nhờ đó, tốc độ build dự án được cải thiện rõ rệt, từ vài phút có thể giảm xuống chỉ còn vài chục giây!

Trong hệ sinh thái GitHub Actions hiện tại, bạn chỉ cần thêm thuộc tính `cache: 'maven'` vào action cài đặt Java (`actions/setup-java@v4`) là tính năng thần thánh này sẽ tự động được kích hoạt.

---

## Phần 2: Hướng dẫn thực hành (Lấy điểm Bài 6)

Để chứng minh tính hiệu quả của Caching, chúng ta sẽ làm đúng theo yêu cầu đề bài: Thực hiện đo lường tốc độ (Benchmark) thông qua 2 lần push code.

### Bước 1: Chạy Workflow KHÔNG có Caching (Lần 1)
Tôi đã tạo sẵn file cấu hình `.github/workflows/bai06.yml` cho bạn, trong đó **cố tình tắt** tính năng cache.

1. Bạn mở Terminal và gõ:
   ```bash
   git add .
   git commit -m "Bai 06 - Chay lan 1 khong co cache"
   git push
   ```
2. Mở GitHub, vào tab **Actions**, chọn tiến trình mang tên `Bai 06 - Optimization & Caching`.
3. Khi tiến trình chạy xong, bạn hãy ghi lại **tổng thời gian thực thi** (ví dụ: 1m 20s).
4. Bấm vào mở log của bước `Build Project`. Bạn sẽ thấy hàng trăm dòng log ghi chữ `Downloading from central: ...` và `Downloaded from central: ...`. Điều này chứng tỏ Maven đang phải cong lưng tải từng file một từ mạng về.

### Bước 2: Bật Caching và chạy lại (Lần 2)
1. Quay lại phần mềm IntelliJ IDEA, mở file `.github/workflows/bai06.yml`.
2. Tìm đến bước `Set up JDK 17` và thêm dòng `cache: 'maven'` vào dưới phần `with:`. Đoạn code sau khi sửa sẽ trông như thế này:
   ```yaml
    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
        cache: 'maven'  # <-- BẠN PHẢI TỰ TAY THÊM DÒNG NÀY VÀO
   ```
3. Lưu file lại. Ra Terminal đẩy code lên một lần nữa:
   ```bash
   git add .
   git commit -m "Bai 06 - Bat tinh nang cache va chay lan 2"
   git push
   ```
4. Lên GitHub Actions chờ tiến trình thứ hai này chạy xong. Bạn sẽ thấy **thời gian thực thi giảm đi đáng kể** so với lần 1!
5. Bấm vào tiến trình đó, mở rộng log của bước `Set up JDK 17`, bạn sẽ thấy một dòng log báo đại loại như: `Cache restored from key: setup-java-maven-...`. 
6. Mở log của bước `Build Project`. Lần này bạn sẽ KHÔNG còn thấy các dòng chữ `Downloading from central` xuất hiện chi chít nữa, vì toàn bộ thư viện đã được lấy trực tiếp từ ổ cứng (cache).

📸 **Yêu cầu báo cáo:** Hãy chụp màn hình tổng thời gian của 2 lần chạy để so sánh, và chụp màn hình log có chữ `Cache restored` chứng minh cache đã hoạt động thành công để nộp cho giảng viên.
