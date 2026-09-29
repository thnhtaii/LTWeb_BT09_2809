# BÁO CÁO BÀI TẬP: SPRING BOOT 4 + SPRING SECURITY + MAPSTRUCT + THYMELEAF + CLOUDINARY

## 1. Mục tiêu và Chức năng hoàn thành

Dự án triển khai hoàn chỉnh theo toàn bộ hướng dẫn trong tài liệu **`HƯỚNG DẪN SPRING BOOT + SECURITY`**:

### A. Authentication & Security
- [x] **Đăng ký (Register)**: Người dùng nhập thông tin đăng ký -> lưu tài khoản với trạng thái `enabled = false` -> mã hóa OTP và gửi email xác nhận.
- [x] **Gửi & Xác nhận OTP (Verify OTP)**: Tạo mã OTP ngẫu nhiên 6 số, mã hóa qua `BCryptPasswordEncoder` lưu vào bảng `otp_tokens`, thời hạn 5 phút, giới hạn 5 lần thử -> kích hoạt tài khoản `enabled = true`.
- [x] **Gửi lại OTP (Resend OTP)**: Cho phép tạo mới và gửi lại mã xác thực nếu chưa nhận được hoặc hết hạn.
- [x] **Đăng nhập (Login)**: Xác thực tài khoản với Spring Security qua Session, mã hóa mật khẩu bằng `BCrypt`.
- [x] **Đăng xuất (Logout)**: Hủy phiên (Invalidate Session), xóa cookie `JSESSIONID`.
- [x] **Quên mật khẩu (Forgot Password)**: Nhập email -> gửi OTP xác thực.
- [x] **Xác thực OTP & Đổi mật khẩu (Reset Password)**: Kiểm tra mã OTP hợp lệ và cập nhật mật khẩu mới.
- [x] **Phân quyền & Kiểm soát truy cập**:
  - Công khai: `/`, `/login`, `/register`, `/verify-otp`, `/forgot-password`, `/reset-password`, `/resend-register-otp`, `/css/**`, `/js/**`.
  - Quyền Admin: `/users/**` (chỉ role `ROLE_ADMIN` mới có quyền truy cập).
  - Quyền Đã đăng nhập: `/products/**` (yêu cầu người dùng đã đăng nhập).

### B. Quản lý Người dùng (User Management)
- [x] **CRUD User**: Thêm mới, cập nhật thông tin (Họ tên, Email, Role, Enabled), Xóa người dùng.
- [x] **Tìm kiếm & Phân trang**: Tìm kiếm theo Username, Email, Họ tên kết hợp phân trang dữ liệu.
- [x] **Thống kê**: Đếm tổng số User trong hệ thống và đếm số Product thuộc về từng User.

### C. Quản lý Sản phẩm (Product Management)
- [x] **CRUD Product**: Thêm sản phẩm kèm tải ảnh, sửa thông tin, xóa sản phẩm (tự động xóa ảnh cũ trên Cloudinary).
- [x] **Tìm kiếm & Phân trang**: Tìm kiếm theo tên sản phẩm, mô tả và phân trang dữ liệu.
- [x] **Upload ảnh Cloudinary**: Tích hợp Cloudinary SDK để tải ảnh sản phẩm lên đám mây và lưu đường dẫn ảnh kèm publicId.
- [x] **Quan hệ User - Product**: 1 User sở hữu nhiều Product (`@ManyToOne`, `@OneToMany`).

### D. MapStruct & Kiến trúc
- [x] **UserMapper**: Chuyển đổi giữa `User` (Entity) và `UserDTO`.
- [x] **ProductMapper**: Chuyển đổi giữa `Product` (Entity) và `ProductDTO`.
- [x] **Mô hình kiến trúc**: MVC + Service Layer + Repository Layer (Spring Data JPA) + DTO/Mapper.

---

## 2. Công nghệ sử dụng

| Thành phần | Công nghệ |
| :--- | :--- |
| **Backend** | Spring Boot 4.1.1 |
| **Security** | Spring Security 6.x / 7.x |
| **Java** | JDK 21 / JDK 26 |
| **Database** | Microsoft SQL Server |
| **ORM** | Spring Data JPA / Hibernate |
| **View** | Thymeleaf + Thymeleaf Layout Dialect + Thymeleaf Extras Spring Security |
| **Mapper** | MapStruct 1.6.3 + MapStruct Processor |
| **Email** | Spring Mail (Gmail SMTP) |
| **Image Storage** | Cloudinary (`cloudinary-http5:2.0.0`) |
| **Validation** | Jakarta Validation |
| **Build Tool** | Maven 3.9+ |

---

## 3. Cấu trúc thư mục dự án

```text
BT09_2809/
├── .env                                       # Cấu hình biến môi trường kết nối DB, Mail, Cloudinary
├── pom.xml                                    # Khai báo dependency và plugin MapStruct
├── README.md                                  # Tài liệu hướng dẫn
└── src/main/
    ├── java/vn/iotstar/
    │   ├── Bt092809Application.java          # Spring Boot main class
    │   ├── config/
    │   │   ├── CloudinaryConfig.java          # Cấu hình Bean Cloudinary
    │   │   ├── DataInitializer.java           # Tự động nạp Role và tài khoản khởi tạo
    │   │   ├── EncodingConfig.java            # Cấu hình CharacterEncodingFilter UTF-8
    │   │   └── SecurityConfig.java            # Cấu hình SecurityFilterChain, FormLogin, Session
    │   ├── controller/
    │   │   ├── AuthController.java            # Điều hướng Login, Register, OTP, Forgot Password
    │   │   ├── ErrorController.java           # Xử lý trang lỗi
    │   │   ├── HomeController.java            # Trang chủ và thống kê Dashboard
    │   │   ├── ProductController.java         # Quản lý CRUD Sản phẩm
    │   │   └── UserController.java            # Quản lý CRUD Người dùng (Admin)
    │   ├── dto/
    │   │   ├── ForgotPasswordDTO.java
    │   │   ├── LoginDTO.java
    │   │   ├── ProductDTO.java
    │   │   ├── RegisterDTO.java
    │   │   ├── ResetPasswordDTO.java
    │   │   ├── UserDTO.java
    │   │   └── VerifyOtpDTO.java
    │   ├── entity/
    │   │   ├── OtpToken.java                  # Entity bảng otp_tokens
    │   │   ├── Product.java                   # Entity bảng products
    │   │   ├── Role.java                      # Entity bảng roles
    │   │   └── User.java                      # Entity bảng users
    │   ├── mapper/
    │   │   ├── ProductMapper.java             # MapStruct Product <-> ProductDTO
    │   │   └── UserMapper.java                # MapStruct User <-> UserDTO
    │   ├── repository/
    │   │   ├── OtpTokenRepository.java
    │   │   ├── ProductRepository.java
    │   │   ├── RoleRepository.java
    │   │   └── UserRepository.java
    │   ├── security/
    │   │   ├── CustomUserDetails.java         # Triển khai UserDetails
    │   │   └── CustomUserDetailsService.java  # Tải User từ CSDL
    │   └── service/
    │       ├── AuthService.java
    │       ├── CloudinaryService.java
    │       ├── CloudinaryUploadResult.java
    │       ├── EmailService.java
    │       ├── OtpService.java
    │       ├── ProductService.java
    │       ├── UserService.java
    │       └── impl/
    │           ├── AuthServiceImpl.java
    │           ├── CloudinaryServiceImpl.java
    │           ├── EmailServiceImpl.java
    │           ├── OtpServiceImpl.java
    │           ├── ProductServiceImpl.java
    │           └── UserServiceImpl.java
    └── resources/
        ├── application.properties
        ├── static/
        │   └── css/
        │       └── app.css                    # CSS giao diện ứng dụng
        └── templates/
            ├── auth/
            │   ├── forgot-password.html       # Giao diện quên mật khẩu
            │   ├── login.html                 # Giao diện đăng nhập
            │   ├── register.html              # Giao diện đăng ký
            │   ├── reset-password.html        # Giao diện đặt lại mật khẩu với OTP
            │   └── verify-otp.html            # Giao diện xác thực OTP đăng ký
            ├── fragments/
            │   ├── footer.html
            │   └── header.html
            ├── layouts/
            │   └── layout.html                # Layout dùng chung cho các view
            ├── products/
            │   ├── form.html                  # Form thêm/sửa sản phẩm kèm tải ảnh
            │   └── list.html                  # Danh sách sản phẩm, tìm kiếm, phân trang
            ├── users/
            │   ├── form.html                  # Form thêm/sửa người dùng
            │   └── list.html                  # Danh sách người dùng, tìm kiếm, phân trang
            ├── error.html
            └── home.html
```

---

## 4. Cấu hình CSDL và Môi trường (`.env` & `application.properties`)

### File `.env` mẫu:
```properties
# ===============================
# DATABASE
# ===============================
DB_URL=jdbc:sqlserver://localhost:1433;databaseName=webst3;encrypt=false;trustServerCertificate=true;sslProtocol=TLSv1.2;characterEncoding=UTF-8
DB_USERNAME=sa
DB_PASSWORD=123456

# ===============================
# JPA
# ===============================
DDL_AUTO=update
SHOW_SQL=true

# ===============================
# SERVER
# ===============================
SERVER_PORT=8080

# ===============================
# SMTP
# ===============================
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password

# ===============================
# CLOUDINARY
# ===============================
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret
```

---

## 5. Hướng dẫn chạy và Kiểm thử ứng dụng

### Bước 1: Khởi động SQL Server & tạo Database
Khởi động dịch vụ SQL Server và tạo cơ sở dữ liệu `webst3` (hoặc tên theo cấu hình `.env`):
```sql
CREATE DATABASE webst3;
```

### Bước 2: Biên dịch và chạy ứng dụng
Mở terminal tại thư mục gốc của dự án:
```bash
mvn clean compile
mvn spring-boot:run
```
Hoặc trong IDE (Eclipse / STS / VSCode): Chuột phải vào project -> `Run As` -> `Spring Boot App`.

### Bước 3: Kiểm thử các chức năng

1. **Khởi tạo dữ liệu**: Khi chạy, ứng dụng tự động kiểm tra và tạo 2 Role (`ROLE_USER`, `ROLE_ADMIN`) cùng các tài khoản mẫu:
   - **Admin**: `admin` / mật khẩu: `123456`
   - **User**: `user01` / mật khẩu: `123456`

2. **Quy trình Đăng ký & Kích hoạt tài khoản**:
   - Truy cập `http://localhost:8080/register`.
   - Điền thông tin và bấm **Đăng ký & nhận OTP**.
   - Kiểm tra hòm thư email nhận mã OTP 6 số.
   - Nhập OTP tại trang `http://localhost:8080/verify-otp` để kích hoạt tài khoản thành công.
   - Nếu chưa nhận được mã, bấm **Gửi lại OTP**.

3. **Quy trình Quên mật khẩu**:
   - Truy cập `http://localhost:8080/forgot-password`.
   - Nhập email đăng ký và bấm **Gửi OTP**.
   - Chuyển sang `http://localhost:8080/reset-password`, nhập mã OTP cùng mật khẩu mới và xác nhận.

4. **Quản lý Sản phẩm (Products)**:
   - Đăng nhập vào hệ thống.
   - Truy cập `/products` để xem danh sách, tìm kiếm sản phẩm theo từ khóa và phân trang.
   - Bấm **+ Thêm Product** để thêm sản phẩm và chọn ảnh upload trực tiếp lên Cloudinary.
   - Sửa hoặc Xóa sản phẩm.

5. **Quản lý Người dùng (Users - Dành cho Admin)**:
   - Đăng nhập bằng tài khoản Admin (`admin` / `123456`).
   - Truy cập `/users` để xem danh sách toàn bộ người dùng, số lượng sản phẩm của từng người dùng.
   - Tìm kiếm, phân trang, thêm mới user hoặc sửa/xóa user.
