# Bài 4: Kiểm thử đa hệ điều hành với Matrix Strategy trong GitHub Actions

## Phần 1: Lý Thuyết Trọng Tâm

### 1. Vấn đề "It works on my machine" (Nó chạy được trên máy tôi)
Đây là một câu nói kinh điển trong ngành phần mềm. Một đoạn code có thể chạy hoàn hảo trên máy tính của người viết code (ví dụ: máy Windows), nhưng lại lỗi tung tóe khi mang sang máy của đồng nghiệp (MacBook) hoặc đẩy lên máy chủ triển khai (Linux). 

Nguyên nhân phổ biến nhất gây ra sự cố này thường là **Đường dẫn tệp (File Path)**: Windows sử dụng dấu gạch chéo ngược (`\`) làm dải phân cách thư mục, trong khi Linux và macOS sử dụng dấu gạch chéo xuôi (`/`).

### 2. Matrix Strategy trong GitHub Actions là gì?
Thay vì bạn phải vất vả tạo 3 file workflow cấu hình riêng biệt cho 3 hệ điều hành, **Matrix Strategy (Chiến lược Ma trận)** cho phép bạn khai báo danh sách các OS. 
GitHub Actions sẽ **tự động nhân bản** tiến trình (job) của bạn và chạy song song tất cả các hệ điều hành đó cùng một lúc!

### 3. Tại sao phải thêm `fail-fast: false`?
Mặc định, khi chạy Matrix Strategy, nếu GitHub phát hiện có bất kỳ một hệ điều hành nào bị lỗi (như Mac hoặc Ubuntu lỗi), nó sẽ **lập tức hủy luôn tất cả các hệ điều hành còn lại đang chạy** (Báo Canceled) để tiết kiệm tài nguyên máy chủ.
Để tắt tính năng này (cho phép các OS khác tiếp tục chạy đến cùng để báo kết quả Xanh/Đỏ chi tiết), ta bắt buộc phải thêm tùy chọn `fail-fast: false` vào cấu hình matrix.

### 4. Công cụ giải quyết: `java.nio.file.Path`
Để khắc phục bài toán khác biệt đường dẫn, Java cung cấp API `java.nio.file.Paths`. API này sẽ tự động nhận diện hệ điều hành đang chạy để dùng `/` hay `\` mà lập trình viên không cần bận tâm.

---

## Phần 2: Hướng dẫn thực hành chi tiết (Các bước đã làm)

### Bước 1: Khởi tạo và tạo lỗi "Hardcoded Path"
Trong thư mục `Bai04/src/test/java/com/fileservice`, ta tạo file `PathTest.java`.
Mục đích là tạo ra một bài Test **chỉ đúng trên Windows và chắc chắn sai trên Linux/Mac**.

**Đoạn code ban đầu cố tình gây lỗi:**
```java
    @Test
    public void testWindowsHardcodedPath() {
        // Lấy đường dẫn chuẩn theo hệ điều hành hiện tại
        Path path = Paths.get("config", "app.properties");
        
        // LỖI SAI CỐ Ý: So sánh với một chuỗi bị đóng cứng theo kiểu Windows (\)
        // Dòng này sẽ PASS trên Windows (vì path.toString() ra config\app.properties)
        // Nhưng sẽ FAILED trên Linux/Mac (vì path.toString() ra config/app.properties)
        assertEquals("config\\app.properties", path.toString());
    }
```

### Bước 2: Tạo Workflow sử dụng Matrix Strategy
Tại thư mục `.github/workflows/`, ta tạo file `bai04.yml` với nội dung cấu hình Matrix chạy trên 3 nền tảng:
```yaml
jobs:
  build:
    strategy:
      fail-fast: false  # Bắt buộc chạy hết tất cả OS, không tự động huỷ
      matrix:
        os: [ubuntu-latest, windows-latest, macos-latest]
        
    runs-on: ${{ matrix.os }} 
    # Các bước cài đặt JDK và chạy mvn test được cấu hình bên dưới...
```

### Bước 3: Quan sát trên GitHub Actions
Khi đẩy (push) code này lên GitHub, vào tab **Actions**, bạn sẽ thấy Job `build` tự động tách thành 3 luồng chạy:
- **`windows-latest`**: Xanh lá 🟢 (Vì hệ điều hành Windows sinh ra dấu `\` giống chuỗi đã code cứng).
- **`ubuntu-latest`** & **`macos-latest`**: Báo đỏ 🔴 (Vì hệ thống Linux sinh ra dấu `/` dẫn đến bài test `assertEquals` bị sai lệch).
Đây chính là bằng chứng sống động nhất minh chứng cho vấn đề "It works on my machine".

### Bước 4: Tái cấu trúc (Refactor) để vượt qua mọi môi trường
Quay lại file `PathTest.java`, ta sửa lỗi bằng cách không dùng chuỗi tĩnh chứa `\` nữa, mà để Java tự động sinh ra đường dẫn bằng API `Paths` cho cả 2 vế so sánh:

**Code đã sửa chuẩn (Cross-Platform - Đa nền tảng):**
```java
    @Test
    public void testCrossPlatformPath() {
        String folder = "config";
        String fileName = "app.properties";
        
        // Java tự động nhận diện OS
        Path path = Paths.get(folder, fileName); 
        
        // Cách so sánh an toàn: so sánh thông qua API, không so sánh chuỗi tĩnh
        Path expectedPath = Paths.get("config", "app.properties");
        
        assertEquals(expectedPath.toString(), path.toString());
    }
```

Sau khi lưu và push đoạn code này lên, GitHub Actions chạy lại và **cả 3 hệ điều hành đều Passed màu xanh lá cây!** 🎉
