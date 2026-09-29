# HƯỚNG DẪN TEST YÊU CẦU 2 - JWT VỚI NIMBUS JOSE + JWT

## 1. Mục tiêu

Yêu cầu 2 thực hiện:

> Sử dụng thư viện **Nimbus JOSE + JWT** thay thế thư viện **JJWT** ở Yêu cầu 1.

Các chức năng của hệ thống vẫn giữ nguyên:

1. Tạo tài khoản.
2. Đăng nhập.
3. Sinh JWT sau khi đăng nhập thành công.
4. Dùng JWT để lấy thông tin người dùng đang đăng nhập.
5. Dùng JWT để lấy danh sách người dùng.

Điểm thay đổi chính:

```text
YÊU CẦU 1
JJWT
│
├── Jwts.builder()
├── Jwts.parser()
└── SecretKey


          ↓ THAY THẾ ↓


YÊU CẦU 2
Nimbus JOSE + JWT
│
├── JWTClaimsSet
├── SignedJWT
├── MACSigner
└── MACVerifier
```

---

# 2. Kiểm tra thư viện Nimbus

Trong `pom.xml` phải có:

```xml
<!-- Nimbus JOSE + JWT -->
<dependency>
    <groupId>com.nimbusds</groupId>
    <artifactId>nimbus-jose-jwt</artifactId>
    <version>10.5</version>
</dependency>
```

Không còn các thư viện JJWT:

```text
jjwt-api
jjwt-impl
jjwt-jackson
```

Sau khi sửa `pom.xml`, trong STS:

```text
Right Click project
→ Maven
→ Update Project...
→ chọn JWT_springboot3
→ OK
```

---

# 3. Kiểm tra JwtService

`JwtService.java` phải sử dụng các class của Nimbus:

```java
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
```

Không còn:

```java
import io.jsonwebtoken.*;
```

Nimbus được sử dụng để:

```text
JWTClaimsSet
      ↓
Tạo Claims/Payload

JWSHeader
      ↓
Tạo Header với HS256

SignedJWT
      ↓
Tạo JWT

MACSigner
      ↓
Ký JWT bằng Secret Key

MACVerifier
      ↓
Xác thực chữ ký JWT
```

---

# 4. Chuẩn bị Database

Khởi động MySQL.

Database sử dụng:

```sql
CREATE DATABASE jwt_springboot3;
```

Nếu database đã được tạo khi làm Yêu cầu 1 thì không cần tạo lại.

Có thể kiểm tra:

```sql
SHOW DATABASES;
```

Sau đó:

```sql
USE jwt_springboot3;

SELECT * FROM users;
```

Dữ liệu user của Yêu cầu 1 có thể tiếp tục được sử dụng.

---

# 5. Kiểm tra application.properties

Mở:

```text
src/main/resources/application.properties
```

Cấu hình:

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

Trong đó:

```text
security.jwt.secret-key
```

là Secret Key được Nimbus sử dụng để ký và xác thực JWT.

```text
security.jwt.expiration-time=3600000
```

tương ứng thời gian hiệu lực:

```text
3.600.000 ms = 1 giờ
```

---

# 6. Chạy Project

Trong STS:

```text
Right Click JWT_springboot3
→ Run As
→ Spring Boot App
```

Nếu project chạy thành công, server hoạt động tại:

```text
http://localhost:8005
```

Giữ Spring Boot chạy trong STS trong suốt quá trình test.

---

# 7. Mở CMD để test

Trên Windows:

```text
Windows + R
→ nhập cmd
→ Enter
```

Thực hiện test theo thứ tự:

```text
Create Account
      ↓
Login
      ↓
Nimbus tạo JWT
      ↓
Copy JWT
      ↓
GET /users/me
      ↓
GET /users
```

---

# 8. TEST 1 - Tạo tài khoản

Trong CMD chạy:

```bat
curl -X POST http://localhost:8005/auth/signup ^
-H "Content-Type: application/json" ^
-d "{\"email\":\"user2@gmail.com\",\"password\":\"123456\",\"fullName\":\"Nguyen Van B\"}"
```

Request tương ứng:

```text
POST /auth/signup
```

với dữ liệu:

```json
{
    "email": "user2@gmail.com",
    "password": "123456",
    "fullName": "Nguyen Van B"
}
```

## Kết quả mong đợi

Server trả về thông tin tài khoản vừa tạo.

Ví dụ:

```json
{
    "id": 2,
    "fullname": "Nguyen Van B",
    "email": "user2@gmail.com",
    "images": ""
}
```

Có thể kiểm tra trong MySQL:

```sql
USE jwt_springboot3;

SELECT * FROM users;
```

---

# 9. TEST 2 - Đăng nhập và tạo JWT bằng Nimbus

Sau khi tạo tài khoản, chạy:

```bat
curl -X POST http://localhost:8005/auth/login ^
-H "Content-Type: application/json" ^
-d "{\"email\":\"user2@gmail.com\",\"password\":\"123456\"}"
```

Request:

```text
POST /auth/login
```

Dữ liệu đăng nhập:

```json
{
    "email": "user2@gmail.com",
    "password": "123456"
}
```

## Quá trình xử lý

Sau khi email và password được xác thực:

```text
AuthenticationController
        ↓
AuthenticationService
        ↓
AuthenticationManager
        ↓
Đăng nhập thành công
        ↓
JwtService.generateToken()
        ↓
Nimbus JOSE + JWT
        ↓
JWTClaimsSet
        ↓
JWSHeader (HS256)
        ↓
SignedJWT
        ↓
MACSigner
        ↓
JWT
```

## Kết quả mong đợi

Server trả về:

```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "expiresIn": 3600000
}
```

Trong đó:

- `token`: JWT được tạo bằng Nimbus.
- `expiresIn`: thời gian hiệu lực của JWT.

Copy toàn bộ giá trị của:

```text
token
```

Ví dụ:

```text
eyJhbGciOiJIUzI1NiJ9....
```

Không copy:

```text
"token":
```

---

# 10. TEST 3 - Lấy User đang đăng nhập

Sử dụng JWT vừa nhận được.

Chạy:

```bat
curl -X GET http://localhost:8005/users/me ^
-H "Authorization: Bearer YOUR_TOKEN"
```

Thay:

```text
YOUR_TOKEN
```

bằng JWT nhận được từ `/auth/login`.

Ví dụ:

```bat
curl -X GET http://localhost:8005/users/me ^
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

## Quá trình xử lý

JWT được gửi lên bằng:

```text
Authorization: Bearer <JWT>
```

Sau đó:

```text
Request
   ↓
JwtAuthenticationFilter
   ↓
Lấy Bearer Token
   ↓
JwtService.extractUsername()
   ↓
SignedJWT.parse()
   ↓
Lấy email từ JWT
   ↓
Tìm User
   ↓
JwtService.isTokenValid()
   ↓
MACVerifier
   ↓
Kiểm tra chữ ký + thời gian hết hạn
   ↓
SecurityContextHolder
   ↓
UserController
```

## Kết quả mong đợi

Server trả về thông tin user đang đăng nhập.

Ví dụ:

```json
{
    "id": 2,
    "fullname": "Nguyen Van B",
    "email": "user2@gmail.com",
    "images": ""
}
```

Email phải đúng với tài khoản vừa login:

```text
user2@gmail.com
```

---

# 11. TEST 4 - Lấy danh sách User

Tiếp tục sử dụng JWT vừa nhận được.

Chạy:

```bat
curl -X GET http://localhost:8005/users ^
-H "Authorization: Bearer YOUR_TOKEN"
```

Thay:

```text
YOUR_TOKEN
```

bằng JWT thật.

Ví dụ:

```bat
curl -X GET http://localhost:8005/users ^
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

## Kết quả mong đợi

Server trả về danh sách user.

Ví dụ:

```json
[
    {
        "id": 1,
        "fullname": "Nguyen Van A",
        "email": "user1@gmail.com"
    },
    {
        "id": 2,
        "fullname": "Nguyen Van B",
        "email": "user2@gmail.com"
    }
]
```

---

# 12. Thứ tự test hoàn chỉnh

## Bước 1 - Signup

```bat
curl -X POST http://localhost:8005/auth/signup ^
-H "Content-Type: application/json" ^
-d "{\"email\":\"user2@gmail.com\",\"password\":\"123456\",\"fullName\":\"Nguyen Van B\"}"
```

↓

## Bước 2 - Login

```bat
curl -X POST http://localhost:8005/auth/login ^
-H "Content-Type: application/json" ^
-d "{\"email\":\"user2@gmail.com\",\"password\":\"123456\"}"
```

↓

Kết quả:

```json
{
    "token": "...",
    "expiresIn": 3600000
}
```

↓

Copy `token`.

↓

## Bước 3 - Current User

```bat
curl -X GET http://localhost:8005/users/me ^
-H "Authorization: Bearer YOUR_TOKEN"
```

↓

## Bước 4 - All Users

```bat
curl -X GET http://localhost:8005/users ^
-H "Authorization: Bearer YOUR_TOKEN"
```

---

# 13. Bảng tổng hợp

| STT | Method | API | JWT | Kết quả |
|---:|---|---|---|---|
| 1 | POST | `/auth/signup` | Không | Tạo tài khoản |
| 2 | POST | `/auth/login` | Không | Đăng nhập và Nimbus tạo JWT |
| 3 | GET | `/users/me` | Bearer Token | Trả user đang đăng nhập |
| 4 | GET | `/users` | Bearer Token | Trả danh sách user |

---

# 14. Điểm khác Yêu cầu 1 và Yêu cầu 2

## Yêu cầu 1 - JJWT

Tạo JWT:

```text
Jwts.builder()
→ claims
→ subject
→ expiration
→ signWith()
→ compact()
```

Xác thực JWT:

```text
Jwts.parser()
→ verifyWith()
→ parseSignedClaims()
```

## Yêu cầu 2 - Nimbus JOSE + JWT

Tạo JWT:

```text
JWTClaimsSet
      ↓
JWSHeader
      ↓
SignedJWT
      ↓
MACSigner
      ↓
sign()
      ↓
serialize()
```

Xác thực JWT:

```text
SignedJWT.parse()
      ↓
MACVerifier
      ↓
verify()
      ↓
JWTClaimsSet
      ↓
Kiểm tra subject
      ↓
Kiểm tra expiration
```

Các API bên ngoài không thay đổi.

Thay đổi nằm ở thư viện được sử dụng bên trong `JwtService`.

---

# 15. Kết quả hoàn thành Yêu cầu 2

Yêu cầu 2 hoàn thành khi chạy được luồng:

```text
POST /auth/signup
        ↓
Tạo User

POST /auth/login
        ↓
Xác thực tài khoản
        ↓
Nimbus tạo JWT
        ↓
Trả JWT cho Client

GET /users/me
Authorization: Bearer <JWT>
        ↓
Nimbus xác thực JWT
        ↓
Trả User hiện tại

GET /users
Authorization: Bearer <JWT>
        ↓
Nimbus xác thực JWT
        ↓
Trả danh sách User
```

Đồng thời kiểm tra source code:

```text
pom.xml
    ↓
có com.nimbusds:nimbus-jose-jwt

JwtService.java
    ↓
có com.nimbusds.*

không còn
    ↓
io.jsonwebtoken.*
```

Nếu 4 API trên hoạt động và JWT được tạo/xác thực bằng Nimbus thì Yêu cầu 2 hoàn thành.