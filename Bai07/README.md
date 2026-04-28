# Bài 7: Automated Code Review via Pull Request

## Phần 1: Lý Thuyết Trọng Tâm

### 1. Tại sao cần Automated Code Review?
Trong một nhóm phát triển, khi bạn nộp code mới (Pull Request - PR), các lập trình viên khác (Reviewer) sẽ phải vào đọc code của bạn và để lại bình luận nhận xét.
Nếu Reviewer phải tốn thời gian đi soi và nhắc nhở những lỗi nhỏ nhặt (như thiếu dấu cách, sai chuẩn đặt tên, code thò ra thụt vào sai) thì năng suất sẽ rất kém. 
**Automated Code Review** giải quyết việc này bằng cách dùng một con Bot chạy tự động. Nó sẽ dò lỗi format (thông qua Checkstyle) và tự động bình luận (comment) ngay lập tức vào đúng dòng code bạn viết sai.

### 2. Branch Protection (Bảo vệ nhánh)
Ngay cả khi Bot đã cảnh báo lỗi, một lập trình viên bất cẩn vẫn có thể bấm nút "Merge" để đưa code rác vào nhánh chính `main`. 
Để ngăn chặn tuyệt đối, GitHub cung cấp tính năng **Branch Protection Rules**. Tính năng này cho phép bạn "Khoá" nút Merge lại. Nút Merge chỉ sáng lên cho phép bấm khi toàn bộ các bài Test và Checkstyle được xác nhận Xanh lá.

---

## Phần 2: Hướng dẫn thiết lập (Các bước tôi đã chuẩn bị sẵn)

### Bước 1: Khởi tạo dự án Bai07 và cố ý viết code sai chuẩn
Trong thư mục `Bai07`, tôi đã cấu hình `pom.xml` tích hợp sẵn `maven-checkstyle-plugin` (sử dụng bộ tiêu chuẩn khắt khe của Google).
Đồng thời, tạo một file `App.java` chứa đầy các lỗi sai chuẩn cố ý (thụt lề lộn xộn, thiếu dấu cách sau `if`...).

### Bước 2: Tạo Workflow cho Pull Request
Trong file `.github/workflows/bai07-pr-review.yml`, cấu hình đã được thiết lập:
- **Trigger**: `on: pull_request` -> Chỉ kích hoạt khi có ai đó mở PR yêu cầu gộp code vào `main`.
- **Inline Feedback**: Tích hợp công cụ `reviewdog/action-checkstyle` (một công cụ hiện đại và ổn định hơn `dbelyaev` được ví dụ trong đề bài) để cấp quyền cho Bot tự động comment báo lỗi.

---

## Phần 3: Thao tác của bạn (Làm theo để lấy điểm)

### Bước 3: Cấu hình Branch Protection trên GitHub
Phần này đòi hỏi quyền chủ Repository nên **bạn phải tự thao tác trên trình duyệt web**:
1. Mở trang Repo GitHub của bạn, chọn tab **Settings** ở menu ngang trên cùng.
2. Nhìn menu dọc bên trái, chọn **Branches**.
3. Nhấn nút **Add branch protection rule**.
4. Ở ô *Branch name pattern*, điền chữ **`main`**.
5. Tích vào ô **Require a pull request before merging**.
6. Tích tiếp vào ô **Require status checks to pass before merging**.
7. Ngay phía dưới có ô tìm kiếm (Search for status checks), bạn gõ chữ **`review`** (đây là tên của job trong file yml của chúng ta) và bấm chọn nó.
8. Cuộn xuống cuối trang, nhấn **Create** (hoặc Save changes).

### Bước 4: Tạo PR ảo để kiểm chứng (Verification)
Bây giờ chúng ta sẽ đóng vai một lập trình viên khác đang phá hoại code.
Mở Terminal và gõ lần lượt các lệnh sau (nhớ copy paste từng dòng):

```bash
# 1. Đẩy các file tôi vừa tạo lên nhánh main trước
git add .
git commit -m "Khoi tao Bai 07"
git push

# 2. Tạo một nhánh (branch) mới tên là 'feature-bad-code' để giả vờ làm tính năng mới
git checkout -b feature-bad-code

# 3. Mở file Bai07/src/main/java/com/review/App.java, thêm vài dấu cách trắng (space) lộn xộn vào các dòng code để làm thay đổi file. Lưu lại.

# 4. Đẩy nhánh code rác này lên mạng
git add .
git commit -m "Cố tình viết code sai format"
git push -u origin feature-bad-code
```

**Mở Pull Request trên GitHub:**
1. Lên trang chủ Repo GitHub của bạn, bạn sẽ thấy một thông báo màu vàng hiện ra báo có nhánh `feature-bad-code` vừa push. Nhấn nút **Compare & pull request**.
2. Nhấn nút màu xanh **Create pull request**.
3. Chờ khoảng 1 phút để GitHub Actions phân tích.

**QUAN SÁT KẾT QUẢ VÀ CHỤP MÀN HÌNH NỘP BÀI:**
1. **Inline Feedback:** Bạn chuyển sang tab **Files changed** của giao diện PR đó, bạn sẽ thấy con Bot tự động "nhảy" vào comment nhắc nhở ngay bên cạnh dòng code bạn viết sai (Yêu cầu 2 của đề bài).
2. **Branch Protection:** Quay lại tab **Conversation** của PR, cuộn xuống chỗ nút Merge màu xanh. Bạn sẽ thấy nó đã bị **vô hiệu hoá (đổi màu xám, không cho bấm)** kèm dòng chữ báo lỗi màu đỏ (Yêu cầu 3 của đề bài).

📸 **Chụp ảnh màn hình 2 hiện tượng trên** là bạn đã hoàn thành xuất sắc yêu cầu của Bài 7!
