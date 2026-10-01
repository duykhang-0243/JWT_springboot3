# YÊU CẦU 3 – LỊCH SỬ ĐẶT HÀNG VÀ LỌC THEO TRẠNG THÁI

## 1. Mục tiêu

Chức năng cho phép người dùng đã đăng nhập:

- Xem toàn bộ lịch sử đơn hàng của chính mình.
- Xem thông tin chi tiết của từng đơn hàng.
- Theo dõi trạng thái hiện tại của đơn.
- Lọc đơn hàng theo trạng thái.
- Trạng thái hiển thị trên giao diện được lấy trực tiếp từ Database.
- Có thể thay đổi `status` trong bảng `orders` để kiểm tra giao diện cập nhật tương ứng.

Hệ thống hỗ trợ 8 trạng thái:

| Giá trị trong Database | Hiển thị trên giao diện |
|---|---|
| `NEW` | Đơn hàng mới |
| `CONFIRMED` | Đã xác nhận |
| `PREPARING` | Chuẩn bị hàng |
| `SHIPPING` | Vận chuyển |
| `DELIVERING` | Giao hàng |
| `DELIVERED` | Đã giao |
| `CANCELLED` | Đơn hàng hủy |
| `RETURNED` | Đơn hàng hoàn |

---

## 2. Tài khoản kiểm thử

Sử dụng tài khoản:

```text
User ID: 1
Họ tên: Nguyễn Văn A
Email: user1@gmail.com
Mật khẩu: <mật khẩu đã đăng ký>
```

Tài khoản cần có ít nhất một đơn hàng đã được tạo bằng chức năng thanh toán COD ở Yêu cầu 2.

---

## 3. Chuẩn bị dữ liệu kiểm thử

Khởi động MySQL và Spring Boot.

Sau đó đăng nhập bằng:

```text
Email: user1@gmail.com
Password: <mật khẩu>
```

Kiểm tra các đơn hàng của User ID 1:

```sql
SELECT
    id,
    user_id,
    receiver_name,
    total_amount,
    payment_method,
    status,
    created_at
FROM orders
WHERE user_id = 1
ORDER BY created_at DESC;
```

### Kết quả mong đợi

Có ít nhất một đơn hàng của:

```text
user_id = 1
```

Ví dụ:

```text
id:             1
user_id:        1
receiver_name:  Nguyễn Văn A
payment_method: COD
status:         NEW
```

---

## 4. Test xem tất cả lịch sử đơn hàng

Sau khi đăng nhập, chọn:

```text
Lịch sử đơn hàng
```

hoặc truy cập:

```text
http://localhost:8005/orders.html
```

Trang sẽ gọi API:

```text
GET /orders/history
```

kèm JWT của người dùng hiện tại.

### Kết quả mong đợi

Trang hiển thị các đơn hàng thuộc tài khoản đang đăng nhập.

Mỗi đơn hàng hiển thị các thông tin:

```text
Mã đơn hàng
Ngày đặt hàng
Trạng thái
Danh sách sản phẩm
Giá sản phẩm
Số lượng
Thành tiền
Người nhận
Số điện thoại
Địa chỉ
Phương thức thanh toán
Tổng tiền
```

Ví dụ:

```text
Đơn hàng #1                     Đơn hàng mới

Chicken Burrito
65.000 ₫          × 2          130.000 ₫

Người nhận: Nguyễn Văn A
Số điện thoại: 0901234567
Địa chỉ: Thủ Đức, TP.HCM
Thanh toán: COD

Tổng tiền                       130.000 ₫
```

---

## 5. Test trạng thái "Đơn hàng mới"

Trong MySQL:

```sql
UPDATE orders
SET status = 'NEW'
WHERE id = 1;
```

Kiểm tra:

```sql
SELECT id, status
FROM orders
WHERE id = 1;
```

Kết quả:

```text
id = 1
status = NEW
```

Refresh:

```text
http://localhost:8005/orders.html
```

### Kết quả mong đợi

Đơn hàng #1 hiển thị:

```text
Đơn hàng mới
```

Chọn bộ lọc:

```text
Đơn hàng mới
```

Frontend gọi:

```text
GET /orders/history?status=NEW
```

Đơn hàng #1 phải xuất hiện.

---

## 6. Test trạng thái "Đã xác nhận"

Trong MySQL:

```sql
UPDATE orders
SET status = 'CONFIRMED'
WHERE id = 1;
```

Refresh trang lịch sử.

### Kết quả mong đợi

Trạng thái đơn #1 thay đổi thành:

```text
Đã xác nhận
```

Chọn:

```text
Đã xác nhận
```

Frontend gọi:

```text
GET /orders/history?status=CONFIRMED
```

Đơn #1 phải xuất hiện trong kết quả.

Đơn #1 không còn xuất hiện khi chọn bộ lọc:

```text
Đơn hàng mới
```

---

## 7. Test trạng thái "Chuẩn bị hàng"

Chạy:

```sql
UPDATE orders
SET status = 'PREPARING'
WHERE id = 1;
```

Refresh trang.

### Kết quả mong đợi

Hiển thị:

```text
Chuẩn bị hàng
```

Chọn bộ lọc:

```text
Chuẩn bị hàng
```

Frontend gọi:

```text
GET /orders/history?status=PREPARING
```

Đơn #1 phải xuất hiện.

---

## 8. Test trạng thái "Vận chuyển"

Chạy:

```sql
UPDATE orders
SET status = 'SHIPPING'
WHERE id = 1;
```

Refresh trang.

### Kết quả mong đợi

Hiển thị:

```text
Vận chuyển
```

Chọn bộ lọc:

```text
Vận chuyển
```

Frontend gọi:

```text
GET /orders/history?status=SHIPPING
```

Đơn #1 phải xuất hiện.

---

## 9. Test trạng thái "Giao hàng"

Chạy:

```sql
UPDATE orders
SET status = 'DELIVERING'
WHERE id = 1;
```

Refresh trang.

### Kết quả mong đợi

Hiển thị:

```text
Giao hàng
```

Chọn bộ lọc:

```text
Giao hàng
```

Frontend gọi:

```text
GET /orders/history?status=DELIVERING
```

Đơn #1 phải xuất hiện.

---

## 10. Test trạng thái "Đã giao"

Chạy:

```sql
UPDATE orders
SET status = 'DELIVERED'
WHERE id = 1;
```

Refresh trang.

### Kết quả mong đợi

Hiển thị:

```text
Đã giao
```

Chọn bộ lọc:

```text
Đã giao
```

Frontend gọi:

```text
GET /orders/history?status=DELIVERED
```

Đơn #1 phải xuất hiện.

---

## 11. Test trạng thái "Đơn hàng hủy"

Chạy:

```sql
UPDATE orders
SET status = 'CANCELLED'
WHERE id = 1;
```

Refresh trang.

### Kết quả mong đợi

Hiển thị:

```text
Đơn hàng hủy
```

Chọn bộ lọc:

```text
Đơn hàng hủy
```

Frontend gọi:

```text
GET /orders/history?status=CANCELLED
```

Đơn #1 phải xuất hiện.

---

## 12. Test trạng thái "Đơn hàng hoàn"

Chạy:

```sql
UPDATE orders
SET status = 'RETURNED'
WHERE id = 1;
```

Refresh trang.

### Kết quả mong đợi

Hiển thị:

```text
Đơn hàng hoàn
```

Chọn bộ lọc:

```text
Đơn hàng hoàn
```

Frontend gọi:

```text
GET /orders/history?status=RETURNED
```

Đơn #1 phải xuất hiện.

---

## 13. Test bộ lọc "Tất cả"

Sau khi kiểm tra từng trạng thái, chọn:

```text
Tất cả
```

Frontend gọi:

```text
GET /orders/history
```

### Kết quả mong đợi

Hệ thống hiển thị tất cả đơn hàng của tài khoản đang đăng nhập, không phụ thuộc trạng thái.

Ví dụ:

```text
Đơn #1    NEW
Đơn #2    CONFIRMED
Đơn #3    DELIVERED
Đơn #4    CANCELLED
```

đều được hiển thị.

---

## 14. Test đồng thời đủ 8 trạng thái

Nếu Database có ít nhất 8 đơn hàng, có thể đặt mỗi đơn thành một trạng thái khác nhau để quan sát trực quan.

Ví dụ:

```sql
UPDATE orders SET status = 'NEW'
WHERE id = 1;

UPDATE orders SET status = 'CONFIRMED'
WHERE id = 2;

UPDATE orders SET status = 'PREPARING'
WHERE id = 3;

UPDATE orders SET status = 'SHIPPING'
WHERE id = 4;

UPDATE orders SET status = 'DELIVERING'
WHERE id = 5;

UPDATE orders SET status = 'DELIVERED'
WHERE id = 6;

UPDATE orders SET status = 'CANCELLED'
WHERE id = 7;

UPDATE orders SET status = 'RETURNED'
WHERE id = 8;
```

Sau đó kiểm tra:

```sql
SELECT
    id,
    user_id,
    status
FROM orders
ORDER BY id;
```

### Kết quả mong đợi

Trang `orders.html` hiển thị các trạng thái tương ứng:

```text
#1  Đơn hàng mới
#2  Đã xác nhận
#3  Chuẩn bị hàng
#4  Vận chuyển
#5  Giao hàng
#6  Đã giao
#7  Đơn hàng hủy
#8  Đơn hàng hoàn
```

Khi chọn từng bộ lọc, chỉ các đơn có trạng thái tương ứng được hiển thị.

> Lưu ý: Các đơn trên phải thuộc tài khoản đang đăng nhập thì mới xuất hiện trong lịch sử của tài khoản đó.

---

## 15. Test bộ lọc không có dữ liệu

Ví dụ Database không có đơn nào có:

```text
status = RETURNED
```

Chọn:

```text
Đơn hàng hoàn
```

### Kết quả mong đợi

Giao diện hiển thị:

```text
Không có đơn hàng

Không tìm thấy đơn hàng phù hợp với trạng thái này.
```

Hệ thống không phát sinh lỗi.

---

## 16. Test bảo mật lịch sử đơn hàng

Đăng xuất khỏi hệ thống.

JWT trong:

```text
localStorage
```

bị xóa.

Sau đó truy cập:

```text
http://localhost:8005/orders.html
```

### Kết quả mong đợi

Do không có JWT hợp lệ, người dùng không thể lấy dữ liệu lịch sử đơn hàng.

Trang chuyển về:

```text
/index.html
```

để đăng nhập.

API:

```text
GET /orders/history
```

vẫn được Spring Security bảo vệ và yêu cầu:

```text
Authorization: Bearer <JWT>
```

---

## 17. Test mỗi người dùng chỉ xem đơn hàng của mình

Đăng nhập bằng:

```text
user1@gmail.com
```

API lịch sử sử dụng người dùng lấy từ JWT.

Backend thực hiện truy vấn theo:

```text
User + OrderStatus
```

hoặc:

```text
User
```

Do đó tài khoản User ID 1 chỉ nhận các đơn hàng có:

```text
user_id = 1
```

### Kết quả mong đợi

Người dùng không nhìn thấy lịch sử đơn hàng của tài khoản khác.

---

## 18. Kiểm tra toàn bộ trạng thái trong Database

Có thể sử dụng:

```sql
SELECT
    id,
    user_id,
    receiver_name,
    total_amount,
    payment_method,
    status,
    created_at
FROM orders
ORDER BY created_at DESC;
```

Để kiểm tra nhanh số lượng đơn theo trạng thái:

```sql
SELECT
    status,
    COUNT(*) AS total_orders
FROM orders
GROUP BY status;
```

---

## 19. Luồng hoạt động của chức năng

```text
Người dùng đăng nhập
        ↓
JWT được lưu
        ↓
Mở orders.html
        ↓
GET /orders/history
        ↓
Backend xác định User từ JWT
        ↓
OrderService
        ↓
OrderRepository
        ↓
Lấy các đơn của User
        ↓
OrderItemRepository
        ↓
Lấy sản phẩm trong từng đơn
        ↓
OrderResponse
        ↓
Frontend hiển thị lịch sử
```

Khi người dùng chọn trạng thái:

```text
Chọn "Đã giao"
        ↓
status = DELIVERED
        ↓
GET /orders/history?status=DELIVERED
        ↓
Backend lọc theo User + DELIVERED
        ↓
Trả về các đơn phù hợp
        ↓
Frontend hiển thị
```

---

## 20. Test case tổng hợp

| STT | Test case | Dữ liệu | Kết quả mong đợi |
|---|---|---|---|
| 1 | Xem tất cả đơn | Không truyền status | Hiển thị tất cả đơn của user |
| 2 | Đơn mới | `NEW` | Hiển thị "Đơn hàng mới" |
| 3 | Đã xác nhận | `CONFIRMED` | Hiển thị "Đã xác nhận" |
| 4 | Chuẩn bị hàng | `PREPARING` | Hiển thị "Chuẩn bị hàng" |
| 5 | Vận chuyển | `SHIPPING` | Hiển thị "Vận chuyển" |
| 6 | Giao hàng | `DELIVERING` | Hiển thị "Giao hàng" |
| 7 | Đã giao | `DELIVERED` | Hiển thị "Đã giao" |
| 8 | Đơn hủy | `CANCELLED` | Hiển thị "Đơn hàng hủy" |
| 9 | Đơn hoàn | `RETURNED` | Hiển thị "Đơn hàng hoàn" |
| 10 | Không có đơn phù hợp | Trạng thái không có dữ liệu | Hiển thị "Không có đơn hàng" |
| 11 | Không có JWT | Truy cập lịch sử sau logout | Yêu cầu đăng nhập lại |
| 12 | User khác | Đăng nhập tài khoản khác | Không thấy đơn của User ID 1 |

---

## 21. Kết quả kiểm thử

Chức năng được xem là đạt yêu cầu khi:

```text
✓ Hiển thị được lịch sử đặt hàng.

✓ Chỉ hiển thị đơn của người dùng đang đăng nhập.

✓ Hiển thị thông tin chi tiết của từng đơn.

✓ Hiển thị đúng trạng thái lấy từ Database.

✓ Lọc được "Tất cả".

✓ Lọc được "Đơn hàng mới".

✓ Lọc được "Đã xác nhận".

✓ Lọc được "Chuẩn bị hàng".

✓ Lọc được "Vận chuyển".

✓ Lọc được "Giao hàng".

✓ Lọc được "Đã giao".

✓ Lọc được "Đơn hàng hủy".

✓ Lọc được "Đơn hàng hoàn".

✓ Khi thay đổi status trực tiếp trong Database và refresh trang,
  giao diện hiển thị trạng thái mới tương ứng.

✓ API lịch sử đơn hàng được bảo vệ bằng JWT.
```

## Kết luận

Yêu cầu 3 đã hoàn thành chức năng:

```text
Lịch sử đặt hàng
        +
Lọc theo trạng thái
        +
Theo dõi trạng thái đơn từ Database
```

Các trạng thái được hỗ trợ:

```text
NEW
CONFIRMED
PREPARING
SHIPPING
DELIVERING
DELIVERED
CANCELLED
RETURNED
```

Việc thay đổi trường `status` trong bảng `orders` được phản ánh trên giao diện sau khi tải lại dữ liệu, cho phép kiểm thử đầy đủ quá trình thay đổi trạng thái của đơn hàng.