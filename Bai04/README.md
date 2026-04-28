# Bài 4: Kiểm thử đa hệ điều hành với Matrix Strategy trong GitHub Actions

## Phần 1: Lý Thuyết Trọng Tâm

### 1. Vấn đề "It works on my machine" (Nó chạy được trên máy tôi)
Đây là một câu nói kinh điển trong ngành phần mềm. Một đoạn code có thể chạy hoàn hảo trên máy tính của người viết code (ví dụ: máy Windows), nhưng lại lỗi tung tóe khi mang sang máy của đồng nghiệp (MacBook) hoặc đẩy lên máy chủ triển khai (Linux). 

Nguyên nhân phổ biến nhất gây ra sự cố này thường là:
- **Đường dẫn tệp (File Path):** Windows sử dụng dấu gạch chéo ngược (`\`) làm dải phân cách thư mục, trong khi Linux và macOS sử dụng dấu gạch chéo xuôi (`/`).
- **Phân biệt chữ hoa chữ thường (Case Sensitivity):** Hệ thống tệp của Linux phân biệt rõ ràng giữa `File.txt` và `file.txt`, nhưng Windows thì coi nó là một.
- **Biến môi trường và Ký tự kết thúc dòng (Line Endings):** Khác biệt giữa CRLF (Windows) và LF (Linux/Mac).

### 2. Matrix Strategy trong GitHub Actions là gì?
Thay vì bạn phải vất vả tạo 3 file workflow cấu hình riêng biệt cho 3 hệ điều hành, **Matrix Strategy (Chiến lược Ma trận)** cho phép bạn định nghĩa một "ma trận" các biến (như Hệ điều hành, Phiên bản Java, Phiên bản Node.js, v.v.). 

Dựa trên ma trận đó, GitHub Actions sẽ **tự động nhân bản** tiến trình (job) của bạn và chạy song song tất cả các tổ hợp có thể.
*Ví dụ:* Nếu bạn định nghĩa ma trận có 3 OS (Ubuntu, Windows, macOS) và 2 bản Java (17, 21), hệ thống sẽ tự động bung ra thành 6 máy ảo chạy test song song cùng lúc!

### 3. File.separator và java.nio.file.Path
Để khắc phục bài toán khác biệt đường dẫn giữa các OS, ngôn ngữ Java cung cấp sẵn các công cụ độc lập với nền tảng (Platform-independent):
- `File.separator`: Một hằng số tự động biến thành `\` nếu code đang chạy trên Windows, và tự biến thành `/` nếu code đang chạy trên Linux/Mac.
- `java.nio.file.Path` và `Paths.get()`: API hiện đại của Java tự động xử lý và nối các thành phần đường dẫn bằng dấu phân cách chuẩn của hệ điều hành mà nó đang thực thi.

---

## Phần 2: Hướng dẫn thực hành chi tiết

### Bước 1: Khởi tạo thư mục Bai04
1. Bạn tạo một thư mục mới tên là `Bai04`.
2. Khởi tạo file `pom.xml` chứa dependencies cho JUnit 5 giống như Bai03.
3. Tạo cấu trúc thư mục code Java: `src/main/java/` và `src/test/java/`.

### Bước 2: Tạo lỗi cố ý với đường dẫn "cứng" (Hardcoded Path)
Tại thư mục Test, hãy tạo một Unit Test để kiểm tra việc nối chuỗi đường dẫn.
**Yêu cầu:** Bạn sẽ ghép đường dẫn theo kiểu "Windows-only" (sử dụng dấu `\\`).
*Ví dụ tạo file `PathTest.java`:*

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PathTest {
    @Test
    public void testWindowsHardcodedPath() {
        String folder = "config";
        String fileName = "app.properties";
        
        // LỖI SAI: Hardcode dấu phân cách của riêng Windows
        String fullPath = folder + "\\" + fileName; 
        
        // Code này chỉ PASS trên Windows, sẽ FAILED trên Linux/Mac
        assertEquals("config\\app.properties", fullPath);
    }
}
```

### Bước 3: Cập nhật GitHub Actions để áp dụng Matrix Strategy
Mở file `.github/workflows/ci.yml`, sửa lại nội dung job `build` để thêm phần `strategy`:

```yaml
jobs:
  build:
    strategy:
      matrix:
        # Khai báo chạy trên 3 hệ điều hành khác nhau
        os: [ubuntu-latest, windows-latest, macos-latest]
    
    # Biến ${{ matrix.os }} sẽ tự động thay thế bằng từng OS ở trên
    runs-on: ${{ matrix.os }} 
    
    steps:
    - uses: actions/checkout@v4
    
    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
        cache: maven
        
    - name: Chạy Test trên nhiều OS
      run: mvn -B test
      working-directory: ./Bai04
```

### Bước 4: Chạy Pipeline và quan sát lỗi trên GitHub
1. Mở Terminal, chạy các lệnh `git add .`, `git commit -m "Them Bai 04 va Matrix"`, `git push`.
2. Mở tab **Actions** trên GitHub. Lúc này bạn sẽ thấy điều kỳ diệu: Job `build` duy nhất của bạn đã rẽ nhánh thành 3 Job chạy song song (build ubuntu-latest, build windows-latest, build macos-latest).
3. **Quan sát kết quả:** 
   - Tiến trình `windows-latest` sẽ Xanh lá.
   - Tiến trình `ubuntu-latest` và `macos-latest` sẽ **báo lỗi Đỏ (Failed)** do hệ thống Linux/Mac không hiểu đường dẫn `config\app.properties`.
   - 📸 *Hãy chụp lại hình ảnh 1 Xanh 2 Đỏ này để bỏ vào báo cáo chứng minh bạn đã quan sát được vấn đề bất đồng hệ điều hành.*

### Bước 5: Tái cấu trúc (Refactor) để vượt qua mọi môi trường
Bây giờ, bạn quay lại file `PathTest.java` để sửa lỗi. Thay vì dùng `\\`, hãy dùng thư viện tiêu chuẩn của Java.

**Cách giải quyết bằng `Paths` (Khuyên dùng):**
```java
import org.junit.jupiter.api.Test;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PathTest {
    @Test
    public void testCrossPlatformPath() {
        String folder = "config";
        String fileName = "app.properties";
        
        // Java tự động nhận diện OS để dùng '/' hay '\'
        Path path = Paths.get(folder, fileName); 
        
        // Cách so sánh an toàn: không so sánh chuỗi tĩnh nữa, so sánh qua API
        Path expectedPath = Paths.get("config", "app.properties");
        
        assertEquals(expectedPath.toString(), path.toString());
    }
}
```

Sau khi sửa code, bạn tiếp tục `git add`, `git commit`, và `git push`. 
Lần này, bạn lên GitHub kiểm tra và sẽ thấy **cả 3 hệ điều hành đều Passed màu xanh lá cây!** 🎉 Đánh dấu hoàn thành bài tập.
