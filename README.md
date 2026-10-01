# Hướng dẫn kiểm thử hệ thống

## 1. Tài khoản kiểm thử

Sử dụng tài khoản:

| Thông tin | Giá trị |
|---|---|
| User ID | 1 |
| Họ tên | Nguyễn Văn A |
| Email | `user1@gmail.com` |
| Mật khẩu | Sử dụng mật khẩu đã đăng ký cho tài khoản |

---

## 2. Test đăng nhập

### Bước 1: Khởi động hệ thống

Khởi động MySQL và chạy Spring Boot.

Nếu hệ thống chạy thành công, Console sẽ hiển thị:

```text
Tomcat started on port 8005
```

Mở trình duyệt:

```text
http://localhost:8005/
```

### Bước 2: Đăng nhập

Nhập:

```text
Email: user1@gmail.com
Password: <mật khẩu của tài khoản>
```

Nhấn:

```text
Đăng nhập
```

### Kết quả mong đợi

Hệ thống chuyển sang trang chính và hiển thị:

```text
Thông tin tài khoản

ID:     1
Họ tên: Nguyễn Văn A
Email:  user1@gmail.com
```

JWT Token cũng được tạo và lưu trong `localStorage`.

---

## 3. Test trang sản phẩm

Từ trang chính, chọn:

```text
Sản phẩm
```

hoặc truy cập:

```text
http://localhost:8005/products.html
```

### Kết quả mong đợi

Danh sách sản phẩm được hiển thị, ví dụ:

```text
Chicken Burrito
65.000 VNĐ
Tồn kho: 10
[Thêm vào giỏ]

Beef Burrito
75.000 VNĐ
Tồn kho: 8
[Thêm vào giỏ]

Chicken Taco
45.000 VNĐ
Tồn kho: 15
[Thêm vào giỏ]

Beef Taco
55.000 VNĐ
Tồn kho: 12
[Thêm vào giỏ]

Chicken Bowl
70.000 VNĐ
Tồn kho: 10
[Thêm vào giỏ]

Beef Bowl
80.000 VNĐ
Tồn kho: 6
[Thêm vào giỏ]
```

---

## 4. Test thêm sản phẩm vào giỏ hàng

Chọn:

```text
Chicken Burrito
```

Nhấn:

```text
Thêm vào giỏ
```

### Kết quả mong đợi

Hệ thống thông báo:

```text
Đã thêm sản phẩm vào giỏ hàng.
```

Kiểm tra Database:

```sql
SELECT *
FROM cart_items
WHERE user_id = 1;
```

Phải xuất hiện một dòng tương ứng với Chicken Burrito:

```text
user_id  = 1
quantity = 1
```

---

## 5. Test thêm cùng sản phẩm nhiều lần

Tại trang sản phẩm, nhấn:

```text
Thêm vào giỏ
```

Chicken Burrito thêm một lần nữa.

### Kết quả mong đợi

Hệ thống không tạo một `cart_items` mới cho cùng sản phẩm.

Thay vào đó:

```text
quantity: 1 → 2
```

Kiểm tra:

```sql
SELECT *
FROM cart_items
WHERE user_id = 1;
```

Kết quả phải có:

```text
Chicken Burrito
quantity = 2
```

---

## 6. Test trang giỏ hàng

Chọn:

```text
Giỏ hàng
```

hoặc truy cập:

```text
http://localhost:8005/cart.html
```

### Kết quả mong đợi

Hiển thị:

```text
Chicken Burrito

Giá: 65.000 VNĐ
Tồn kho: 10

[-] [2] [+]

Thành tiền: 130.000 VNĐ
```

Tổng tiền:

```text
130.000 VNĐ
```

---

## 7. Test tăng số lượng

Nhấn:

```text
+
```

### Kết quả mong đợi

```text
quantity: 2 → 3
```

Thành tiền:

```text
65.000 × 3
= 195.000 VNĐ
```

Kiểm tra Database:

```sql
SELECT *
FROM cart_items
WHERE user_id = 1;
```

Phải có:

```text
quantity = 3
```

---

## 8. Test giảm số lượng

Nhấn:

```text
-
```

### Kết quả mong đợi

```text
quantity: 3 → 2
```

Tổng tiền trở lại:

```text
130.000 VNĐ
```

Hệ thống không cho giảm số lượng xuống dưới:

```text
1
```

---

## 9. Test giới hạn tồn kho

Chicken Burrito có:

```text
stock = 10
```

Thử tăng số lượng đến:

```text
10
```

### Kết quả mong đợi

```text
quantity = 10
```

Nút `+` bị vô hiệu hóa.

Hệ thống không cho:

```text
quantity > 10
```

Backend cũng kiểm tra tồn kho nên không thể vượt giới hạn bằng cách chỉnh request từ phía trình duyệt.

---

## 10. Chuẩn bị test COD

Để dễ kiểm tra, đặt lại giỏ hàng thành:

```text
Chicken Burrito × 2
```

Giá:

```text
65.000 VNĐ / sản phẩm
```

Tổng tiền trước khi checkout:

```text
65.000 × 2
= 130.000 VNĐ
```

### Kiểm tra Database trước khi đặt hàng

Chạy:

```sql
SELECT id, name, price, stock
FROM products;

SELECT *
FROM cart_items
WHERE user_id = 1;

SELECT *
FROM orders;

SELECT *
FROM order_items;
```

Ghi nhận stock hiện tại của Chicken Burrito.

Ví dụ:

```text
stock trước khi đặt = 10
```

---

## 11. Test thanh toán COD

Tại trang:

```text
http://localhost:8005/cart.html
```

Nhập thông tin nhận hàng.

Ví dụ:

```text
Họ tên người nhận:
Nguyễn Văn A

Số điện thoại:
0901234567

Địa chỉ:
Thủ Đức, TP.HCM
```

Phương thức thanh toán:

```text
Thanh toán khi nhận hàng (COD)
```

Nhấn:

```text
ĐẶT HÀNG COD
```

### Kết quả mong đợi

Hệ thống thông báo:

```text
Đặt hàng thành công!

Mã đơn hàng: #...
Thanh toán: COD
Trạng thái: NEW
```

Sau đó giỏ hàng phải chuyển thành:

```text
Giỏ hàng đang trống

Bạn chưa thêm sản phẩm nào.
```

---

## 12. Kiểm tra bảng orders

Chạy:

```sql
SELECT *
FROM orders
WHERE user_id = 1
ORDER BY id DESC;
```

Đơn vừa tạo phải có:

```text
user_id         = 1
receiver_name   = Nguyễn Văn A
phone           = 0901234567
address         = Thủ Đức, TP.HCM
total_amount    = 130000
payment_method  = COD
status          = NEW
created_at      = thời điểm đặt hàng
```

---

## 13. Kiểm tra order_items

Lấy ID đơn vừa tạo rồi kiểm tra:

```sql
SELECT *
FROM order_items
ORDER BY id DESC;
```

Với Chicken Burrito × 2, phải có dữ liệu tương ứng:

```text
price     = 65000
quantity  = 2
```

Giá `65000` được lưu tại thời điểm đặt hàng để đảm bảo lịch sử đơn không bị ảnh hưởng nếu giá sản phẩm thay đổi sau này.

---

## 14. Kiểm tra tồn kho sau khi đặt COD

Chạy:

```sql
SELECT id, name, price, stock
FROM products
WHERE name = 'Chicken Burrito';
```

Nếu trước khi đặt:

```text
stock = 10
```

và Nguyễn Văn A mua:

```text
quantity = 2
```

thì sau khi đặt:

```text
stock = 8
```

Theo công thức:

```text
stock mới
= stock cũ - quantity

= 10 - 2

= 8
```

---

## 15. Kiểm tra giỏ hàng sau khi đặt

Chạy:

```sql
SELECT *
FROM cart_items
WHERE user_id = 1;
```

### Kết quả mong đợi

Không còn sản phẩm vừa thanh toán trong giỏ của User ID 1.

Điều này xác nhận quy trình:

```text
Cart
 ↓
Checkout COD
 ↓
Order
 ↓
OrderItem
 ↓
Trừ Stock
 ↓
Xóa Cart
```

đã thực hiện thành công.

---

## 16. Test trường hợp giỏ hàng trống

Sau khi đặt hàng thành công, giỏ của Nguyễn Văn A đang trống.

Nếu thực hiện checkout khi không có sản phẩm, Backend phải từ chối tạo đơn.

### Kết quả mong đợi

```text
Giỏ hàng đang trống.
```

Không được tạo thêm dữ liệu trong:

```text
orders
order_items
```

---

## 17. Test đăng xuất

Nhấn:

```text
Đăng xuất
```

### Kết quả mong đợi

JWT bị xóa khỏi:

```text
localStorage
```

và người dùng quay lại trang đăng nhập.

Sau đó thử truy cập:

```text
http://localhost:8005/products.html
```

hoặc:

```text
http://localhost:8005/cart.html
```

khi không có JWT.

Hệ thống phải chuyển người dùng về:

```text
/index.html
```

để đăng nhập.

---

## 18. Kết quả kiểm thử tổng thể

Với tài khoản:

```text
ID:     1
Họ tên: Nguyễn Văn A
Email:  user1@gmail.com
```

luồng kiểm thử hoàn chỉnh là:

```text
Đăng nhập
   ↓
Xác thực JWT
   ↓
Trang chính
   ↓
Xem sản phẩm
   ↓
Thêm Chicken Burrito × 2
   ↓
Giỏ hàng
   ↓
Tổng tiền = 130.000 VNĐ
   ↓
Nhập thông tin nhận hàng
   ↓
Đặt hàng COD
   ↓
Order được tạo
   ↓
status = NEW
   ↓
OrderItem được tạo
   ↓
Stock giảm 10 → 8
   ↓
Cart của User ID 1 được xóa
   ↓
Đặt hàng thành công
```

Nếu toàn bộ kết quả trên đúng, chức năng **giỏ hàng và thanh toán COD** hoạt động đúng theo yêu cầu.