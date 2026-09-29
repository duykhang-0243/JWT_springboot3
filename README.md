# HƯỚNG DẪN TEST YÊU CẦU 1 - JWT

## 1. Chuẩn bị

### Bước 1: Khởi động MySQL

Đảm bảo MySQL đang chạy và đã có database:

```sql
CREATE DATABASE jwt_springboot3;
```

Kiểm tra cấu hình trong:

```text
src/main/resources/application.properties
```

```properties
spring.application.name=JWT_springboot3
server.port=8005

spring.datasource.url=jdbc:mysql://localhost:3306/jwt_springboot3?serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false
spring.datasource.username=root
spring.datasource.password=123456

spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=false

security.jwt.secret-key=3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b
security.jwt.expiration-time=3600000
```

Nếu mật khẩu MySQL trên máy khác `123456`, thay lại cho đúng.

---

## 2. Chạy project

Trong STS:

```text
Right Click project
→ Run As
→ Spring Boot App
```

Nếu chạy thành công, server hoạt động tại:

```text
http://localhost:8005
```

Giữ Spring Boot chạy trong STS.

---

# 3. Mở Terminal để test

Trên Windows:

```text
Windows + R
→ nhập cmd
→ Enter
```

Hoặc mở PowerShell/Terminal.

Các bước test thực hiện theo thứ tự:

```text
Create Account
      ↓
Login
      ↓
Nhận JWT
      ↓
GET /users/me
      ↓
GET /users
```

---

# 4. TEST 1 - Create Account

Mở CMD và chạy:

```bash
curl -X POST http://localhost:8005/auth/signup -H "Content-Type: application/json" -d "{\"email\":\"user1@gmail.com\",\"password\":\"123456\",\"fullName\":\"Nguyen Van A\"}"
```

## Ý nghĩa

Request được gửi tới:

```text
POST /auth/signup
```

Dữ liệu:

```json
{
    "email": "user1@gmail.com",
    "password": "123456",
    "fullName": "Nguyen Van A"
}
```

## Kết quả mong đợi

Server trả về thông tin tài khoản vừa tạo.

Ví dụ:

```json
{
    "id": 1,
    "fullname": "Nguyen Van A",
    "email": "user1@gmail.com",
    "images": ""
}
```

---

# 5. Kiểm tra Database

Sau khi tạo tài khoản, có thể kiểm tra trong MySQL:

```sql
USE jwt_springboot3;

SELECT * FROM users;
```

Phải thấy tài khoản:

```text
user1@gmail.com
```

trong bảng `users`.

---

# 6. TEST 2 - Login

Sau khi đã có tài khoản, chạy:

```bash
curl -X POST http://localhost:8005/auth/login -H "Content-Type: application/json" -d "{\"email\":\"user1@gmail.com\",\"password\":\"123456\"}"
```

Request gửi dữ liệu:

```json
{
    "email": "user1@gmail.com",
    "password": "123456"
}
```

## Kết quả mong đợi

Nếu email và password đúng, server trả về:

```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "expiresIn": 3600000
}
```

Trong đó:

```text
token
```

là JWT được sinh ra sau khi đăng nhập thành công.

Ví dụ:

```text
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMUBnbWFpbC5jb20iLCJpYXQiOjE...
```

Copy toàn bộ giá trị của `token`.

Không copy:

```text
"token":
```

và không copy dấu `" "`.

---

# 7. TEST 3 - GET /users/me

API này dùng để lấy thông tin user đang đăng nhập.

Sau khi login, đã có JWT.

Chạy:

```bash
curl -X GET http://localhost:8005/users/me -H "Authorization: Bearer YOUR_TOKEN"
```

Thay:

```text
YOUR_TOKEN
```

bằng JWT vừa nhận được.

Ví dụ:

```bash
curl -X GET http://localhost:8005/users/me -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

Lưu ý phải có:

```text
Bearer
```

sau đó là một dấu cách rồi mới tới JWT:

```text
Bearer <JWT>
```

## Kết quả mong đợi

Server trả về thông tin user đang đăng nhập.

Ví dụ:

```json
{
    "id": 1,
    "fullname": "Nguyen Van A",
    "email": "user1@gmail.com",
    "images": ""
}
```

Điều này chứng minh JWT đã được sử dụng để xác thực request.

---

# 8. TEST 4 - GET /users

Tiếp tục sử dụng JWT vừa nhận được.

Chạy:

```bash
curl -X GET http://localhost:8005/users -H "Authorization: Bearer YOUR_TOKEN"
```

Thay:

```text
YOUR_TOKEN
```

bằng JWT thật.

Ví dụ:

```bash
curl -X GET http://localhost:8005/users -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

## Kết quả mong đợi

Server trả về danh sách user:

```json
[
    {
        "id": 1,
        "fullname": "Nguyen Van A",
        "email": "user1@gmail.com",
        "images": ""
    }
]
```

---

# 9. Thứ tự test hoàn chỉnh

### Bước 1 - Create Account

```bash
curl -X POST http://localhost:8005/auth/signup -H "Content-Type: application/json" -d "{\"email\":\"user1@gmail.com\",\"password\":\"123456\",\"fullName\":\"Nguyen Van A\"}"
```

↓

### Bước 2 - Login

```bash
curl -X POST http://localhost:8005/auth/login -H "Content-Type: application/json" -d "{\"email\":\"user1@gmail.com\",\"password\":\"123456\"}"
```

↓

Copy:

```text
token
```

↓

### Bước 3 - Current User

```bash
curl -X GET http://localhost:8005/users/me -H "Authorization: Bearer YOUR_TOKEN"
```

↓

### Bước 4 - All Users

```bash
curl -X GET http://localhost:8005/users -H "Authorization: Bearer YOUR_TOKEN"
```

---

# 10. Kết quả cần đạt

| Bước | API | Kết quả |
|---|---|---|
| 1 | `POST /auth/signup` | Tạo được tài khoản |
| 2 | `POST /auth/login` | Nhận được JWT |
| 3 | `GET /users/me` | Trả về user đang đăng nhập |
| 4 | `GET /users` | Trả về danh sách user |

Luồng hoàn chỉnh:

```text
POST /auth/signup
        ↓
Tạo User

POST /auth/login
        ↓
Authentication
        ↓
Generate JWT
        ↓
Nhận Token

GET /users/me
Authorization: Bearer <JWT>
        ↓
Thông tin User hiện tại

GET /users
Authorization: Bearer <JWT>
        ↓
Danh sách User
```

Nếu 4 bước trên đều chạy thành công thì phần demo của Yêu cầu 1 đã hoàn thành.