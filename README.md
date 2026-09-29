# BÁO CÁO DỰ ÁN: XÂY DỰNG HỆ THỐNG QUẢN LÝ SHOP VỚI SPRING BOOT, SPRING SECURITY, MAPSTRUCT & CLOUDINARY

> **Dự án**: BT09_2809  
> **Môn học**: Lập trình Web / Công nghệ phần mềm  
> **Kiến trúc**: Spring Boot MVC + Spring Data JPA + Spring Security + MapStruct + Thymeleaf

---

## 📑 MỤC LỤC
1. [Giới thiệu tổng quan](#1-giới-thiệu-tổng-quan)
2. [Công nghệ sử dụng](#2-công-nghệ-sử-dụng)
3. [Cấu trúc thư mục dự án](#3-cấu-trúc-thư-mục-dự-án)
4. [Các tính năng đã hoàn thiện](#4-các-tính-năng-đã-hoàn-thiện)
5. [Tài khoản mặc định hệ thống](#5-tài-khoản-mặc-định-hệ-thống)
6. [Danh sách Endpoint / URL](#6-danh-sách-endpoint--url)
7. [Hướng dẫn Cài đặt & Cấu hình](#7-hướng-dẫn-cài-đặt--cấu-hình)
8. [Quy trình & Kịch bản kiểm thử chi tiết](#8-quy-trình--kịch-bản-kiểm-thử-chi-tiết)
9. [Các điểm nổi bật & Tối ưu kỹ thuật](#9-các-điểm-nổi-bật--tối-ưu-kỹ-thuật)

---

## 1. GIỚI THIỆU TỔNG QUAN

Dự án triển khai hoàn chỉnh một ứng dụng Web Thương mại điện tử / Quản lý bán hàng thu nhỏ (**IOTSTAR SHOP**) đáp ứng đầy đủ tất cả các yêu cầu trong 3 tài liệu hướng dẫn:
- **Authentication & Authorization**: Xác thực đa năng (đăng nhập bằng Username hoặc Email), phân quyền vai trò (`ROLE_ADMIN`, `ROLE_USER`), bảo vệ phiên bằng Spring Security và mã hóa mật khẩu `BCrypt`.
- **OTP Verification via Email**: Quy trình đăng ký tài khoản và quên mật khẩu gửi mã OTP 6 số qua Email, mã hóa mã OTP trong CSDL với cơ chế giới hạn thời gian (5 phút) và giới hạn số lần thử (5 lần).
- **Product Management**: CRUD sản phẩm, tải ảnh lên Cloudinary (kèm cơ chế Fallback lưu cục bộ `/uploads/`), tìm kiếm đa trường và phân trang (Pagination).
- **User Management (Admin)**: Quản lý người dùng, phân quyền, thống kê số lượng sản phẩm theo từng người dùng.
- **MapStruct & Clean Architecture**: Tách biệt rõ ràng giữa các tầng Entity, DTO, Mapper, Repository, Service và Controller.

---

## 2. CÔNG NGHỆ SỬ DỤNG

| Thành phần | Công nghệ / Thư viện | Phiên bản | Ghi chú |
| :--- | :--- | :--- | :--- |
| **Framework** | Spring Boot | `4.1.1` | Nền tảng phát triển ứng dụng Java |
| **Bảo mật** | Spring Security | `7.x / 6.x` | Xác thực Form Login, Phân quyền Role, Quản lý Session |
| **Ngôn ngữ** | Java | `JDK 21 / 26` | Java Standard Edition |
| **Cơ sở dữ liệu** | Microsoft SQL Server | `2019 / 2022` | Hệ quản trị CSDL quan hệ |
| **ORM / JPA** | Spring Data JPA / Hibernate | Latest | Tự động sinh truy vấn và ánh xạ Entity |
| **Mapping DTO** | MapStruct | `1.6.3` | Sinh mã mapping compile-time hiệu năng cao |
| **Giao diện (View)** | Thymeleaf | `3.x` | Server-side template engine |
| **Layout** | Thymeleaf Layout Dialect | Latest | Tái sử dụng header, footer, layout chung |
| **Security Tags** | Thymeleaf Extras Spring Security | Latest | Ẩn/hiện menu theo quyền người dùng |
| **Lưu trữ ảnh** | Cloudinary SDK | `2.0.0` | Tải ảnh lên CDN Cloudinary + Fallback Local |
| **Gửi Email** | Spring Mail (JavaMailSender) | Latest | Gửi OTP qua Gmail SMTP |
| **Validation** | Jakarta Validation (Hibernate Validator) | Latest | Kiểm tra hợp lệ dữ liệu Form |
| **Công cụ Build** | Apache Maven | `3.9+` | Quản lý phụ thuộc và đóng gói ứng dụng |

---

## 3. CẤU TRÚC THƯ MỤC DỰ ÁN

```text
BT09_2809/
├── .env                                       # Biến môi trường kết nối DB, Mail, Cloudinary
├── pom.xml                                    # Khai báo dependency và plugin MapStruct
├── README.md                                  # Tài liệu hướng dẫn và báo cáo dự án
├── uploads/                                   # Thư mục lưu trữ ảnh cục bộ (fallback)
└── src/main/
    ├── java/vn/iotstar/
    │   ├── Bt092809Application.java          # Lớp khởi chạy chính của Spring Boot
    │   ├── config/
    │   │   ├── CloudinaryConfig.java          # Cấu hình Bean Cloudinary SDK
    │   │   ├── DataInitializer.java           # Tự động nạp Role, User và 20 Product mẫu
    │   │   ├── EncodingConfig.java            # Bộ lọc CharacterEncodingFilter UTF-8
    │   │   ├── SecurityConfig.java            # Cấu hình SecurityFilterChain, FormLogin, Session
    │   │   └── WebMvcConfig.java              # Cấu hình ResourceHandler phục vụ file tĩnh /uploads/**
    │   ├── controller/
    │   │   ├── AuthController.java            # Xử lý Login, Register, Verify OTP, Forgot Password
    │   │   ├── ErrorController.java           # Điều hướng hiển thị trang lỗi tùy chỉnh
    │   │   ├── HomeController.java            # Dashboard thống kê tổng User và Product
    │   │   ├── ProductController.java         # CRUD Sản phẩm, tìm kiếm, phân trang
    │   │   └── UserController.java            # CRUD Người dùng dành cho Admin
    │   ├── dto/
    │   │   ├── ForgotPasswordDTO.java         # DTO yêu cầu quên mật khẩu
    │   │   ├── LoginDTO.java                  # DTO đăng nhập
    │   │   ├── ProductDTO.java                # DTO truyền dữ liệu sản phẩm
    │   │   ├── RegisterDTO.java               # DTO đăng ký tài khoản
    │   │   ├── ResetPasswordDTO.java          # DTO đặt lại mật khẩu với mã OTP
    │   │   ├── UserDTO.java                   # DTO truyền dữ liệu người dùng
    │   │   └── VerifyOtpDTO.java              # DTO xác thực mã OTP
    │   ├── entity/
    │   │   ├── OtpToken.java                  # Entity bảng otp_tokens (lưu OTP đã hash)
    │   │   ├── Product.java                   # Entity bảng products
    │   │   ├── Role.java                      # Entity bảng roles
    │   │   └── User.java                      # Entity bảng users
    │   ├── mapper/
    │   │   ├── ProductMapper.java             # MapStruct ánh xạ Product <-> ProductDTO
    │   │   └── UserMapper.java                # MapStruct ánh xạ User <-> UserDTO
    │   ├── repository/
    │   │   ├── OtpTokenRepository.java        # Thao tác bảng otp_tokens
    │   │   ├── ProductRepository.java         # Thao tác bảng products (kèm fetch join user)
    │   │   ├── RoleRepository.java            # Thao tác bảng roles
    │   │   └── UserRepository.java            # Thao tác bảng users
    │   ├── security/
    │   │   ├── CustomUserDetails.java         # Triển khai UserDetails với thông tin mở rộng
    │   │   └── CustomUserDetailsService.java  # Tìm nạp User từ CSDL bằng Username hoặc Email
    │   └── service/
    │       ├── AuthService.java               # Interface dịch vụ xác thực & OTP
    │       ├── CloudinaryService.java         # Interface upload & xóa ảnh
    │       ├── CloudinaryUploadResult.java    # Record chứa url và publicId của ảnh
    │       ├── EmailService.java              # Interface gửi email xác thực
    │       ├── OtpService.java                # Interface xử lý tạo, mã hóa và verify OTP
    │       ├── ProductService.java            # Interface nghiệp vụ sản phẩm
    │       ├── UserService.java               # Interface nghiệp vụ người dùng
    │       └── impl/
    │           ├── AuthServiceImpl.java       # Triển khai xác thực, đăng ký, quên mật khẩu
    │           ├── CloudinaryServiceImpl.java # Triển khai Cloudinary + Fallback Local
    │           ├── EmailServiceImpl.java      # Triển khai gửi mail qua SMTP + Log Console
    │           ├── OtpServiceImpl.java        # Triển khai hash OTP bằng BCrypt, check expiry & retry
    │           ├── ProductServiceImpl.java    # Triển khai CRUD Sản phẩm
    │           └── UserServiceImpl.java       # Triển khai CRUD Người dùng
    └── resources/
        ├── application.properties             # Cấu hình Spring Boot và import .env
        ├── static/
        │   └── css/
        │       └── app.css                    # CSS giao diện hiện đại, responsive & Modal UI
        └── templates/
            ├── auth/
            │   ├── forgot-password.html       # Giao diện gửi OTP quên mật khẩu
            │   ├── login.html                 # Giao diện đăng nhập
            │   ├── register.html              # Giao diện đăng ký tài khoản mới
            │   ├── reset-password.html        # Giao diện nhập OTP và mật khẩu mới
            │   └── verify-otp.html            # Giao diện xác thực OTP kích hoạt tài khoản
            ├── fragments/
            │   ├── footer.html                # Thanh footer dùng chung
            │   └── header.html                # Thanh navigation bar & hiển thị thông tin user
            ├── layouts/
            │   └── layout.html                # Layout mẫu dùng chung
            ├── products/
            │   ├── form.html                  # Form thêm / chỉnh sửa sản phẩm kèm chọn ảnh
            │   └── list.html                  # Bảng danh sách sản phẩm, tìm kiếm, phân trang & modal xóa
            ├── users/
            │   ├── form.html                  # Form thêm / chỉnh sửa người dùng
            │   └── list.html                  # Bảng danh sách người dùng, đếm product & modal xóa
            ├── error.html                     # Trang thông báo lỗi thân thiện
            └── home.html                      # Trang chủ Dashboard thống kê
```

---

## 4. CÁC TÍNH NĂNG ĐÃ HOÀN THIỆN

### A. Authentication & Security
- [x] **Đăng nhập linh hoạt**: Hỗ trợ đăng nhập bằng **Username** hoặc **Email**.
- [x] **Mã hóa mật khẩu an toàn**: Toàn bộ mật khẩu người dùng được băm bằng `BCryptPasswordEncoder`.
- [x] **Bảo mật phiên (Session Management)**:
  - Giới hạn tối đa 1 session hoạt động cùng lúc trên mỗi tài khoản (`maximumSessions(1)`).
  - Tự động chuyển hướng về `/login?expired` khi hết hạn phiên làm việc.
- [x] **Đăng xuất an toàn**: Xóa thông tin phiên (Invalidate session), xóa Authentication và cookie `JSESSIONID`.
- [x] **Phân quyền truy cập đa cấp (RBAC)**:
  - Trang công khai: `/`, `/login`, `/register`, `/verify-otp`, `/forgot-password`, `/reset-password`, `/resend-register-otp`, `/uploads/**`, `/css/**`.
  - Yêu cầu đăng nhập (`Authenticated`): `/products/**`.
  - Yêu cầu quyền quản trị viên (`hasRole('ADMIN')`): `/users/**`.

### B. Quy trình Xác thực OTP qua Email
- [x] **Mã hóa OTP trong CSDL**: Mã OTP 6 chữ số được băm bằng `BCrypt` trước khi lưu vào bảng `otp_tokens`.
- [x] **Giới hạn thời gian sống (TTL)**: Mã OTP chỉ có hiệu lực trong vòng **5 phút**.
- [x] **Chống tấn công Brute-Force**: Giới hạn tối đa **5 lần thử sai**. Nếu vượt quá 5 lần, mã OTP tự động bị vô hiệu hóa.
- [x] **Quy trình Đăng ký**: Người dùng điền thông tin -> Tạo tài khoản với `enabled = false` -> Sinh OTP và gửi qua Email -> Nhập OTP để kích hoạt `enabled = true`.
- [x] **Gửi lại OTP (Resend OTP)**: Cho phép tạo mã OTP mới nếu mã cũ hết hạn hoặc thất lạc.
- [x] **Quy trình Quên mật khẩu**: Nhập email -> Nhận mã OTP -> Nhập OTP và mật khẩu mới tại màn hình Reset Password.
- [x] **Cơ chế Fallback Console**: Luôn ghi mã OTP ra cửa sổ Terminal (`>> [OTP LOCAL LOG]`) giúp kiểm thử thuận tiện ngay cả khi chưa kết nối SMTP thật.

### C. Quản lý Sản phẩm (Product Management)
- [x] **CRUD Sản phẩm**: Thêm mới, xem chi tiết, cập nhật thông tin và xóa sản phẩm.
- [x] **Upload ảnh đa chế độ**:
  - Hỗ trợ tải trực tiếp lên dịch vụ đám mây **Cloudinary**.
  - Tự động chuyển sang lưu trữ cục bộ tại thư mục `/uploads/` nếu thông tin Cloudinary không hợp lệ (Fallback Mode).
  - Tự động dọn dẹp ảnh cũ khi cập nhật hoặc xóa sản phẩm.
- [x] **Tìm kiếm đa trường**: Tìm kiếm sản phẩm theo tên hoặc mô tả sản phẩm.
- [x] **Phân trang dữ liệu (Pagination)**: Phân trang linh hoạt với các nút chuyển trang `«`, `1`, `2`, `3...`, `»` giữ nguyên trạng thái tìm kiếm.
- [x] **Định dạng giá tiền chuẩn**: Giá sản phẩm được định dạng chuẩn tiền tệ (ví dụ `26,000,000`, loại bỏ 2 số thập phân `.00`).
- [x] **Modal xác nhận xóa**: Thay thế popup trình duyệt mặc định bằng giao diện **Modal Dialog** hiện đại, bo góc mềm mại, có hiệu ứng làm mờ nền (backdrop blur), hỗ trợ phím `Esc` và click ra ngoài để đóng modal.

### D. Quản lý Người dùng (User Management - Admin)
- [x] **CRUD Người dùng**: Thêm mới tài khoản, cập nhật thông tin (Họ tên, Email, Vai trò, Trạng thái hoạt động), xóa tài khoản.
- [x] **Thống kê sản phẩm**: Hiển thị tổng số lượng sản phẩm đang thuộc sở hữu của từng người dùng.
- [x] **Tìm kiếm & Phân trang**: Tìm kiếm người dùng theo Username, Email, Họ tên kết hợp phân trang.
- [x] **Modal xác nhận xóa người dùng**: Hộp thoại cảnh báo trực quan trước khi xóa tài khoản.

### E. Dashboard & Giao diện
- [x] **Thống kê tổng quan**: Hiển thị nhanh tổng số User và tổng số Product trong hệ thống.
- [x] **Header thông minh**: Hiển thị họ tên đầy đủ, username và badge quyền hạn (`[ROLE_ADMIN]` / `[ROLE_USER]`). Tự động ẩn/hiện menu quản trị Users đối với người dùng không phải Admin.
- [x] **Giao diện Responsive**: Tối ưu hiển thị trên cả máy tính để bàn, máy tính bảng và điện thoại di động.

---

## 5. TÀI KHOẢN MẶC ĐỊNH HỆ THỐNG

Hệ thống được cấu hình tự động khởi tạo các tài khoản mẫu khi ứng dụng khởi chạy lần đầu:

| Username | Email | Mật khẩu | Họ và tên | Vai trò (Role) | Quyền hạn |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **`admin`** | `admin@gmail.com` | `123456` | Administrator | `ROLE_ADMIN` | Toàn quyền quản trị hệ thống (`/users`, `/products`, Dashboard) |
| **`user01`** | `user01@gmail.com` | `123456` | Đỗ Thanh Thành Tài | `ROLE_USER` | Quản lý sản phẩm cá nhân (`/products`, Dashboard) |

---

## 6. DANH SÁCH ENDPOINT / URL

### Nhóm Xác thực (Authentication & OTP)
| Phương thức | URL | Mô tả chức năng | Quyền truy cập |
| :--- | :--- | :--- | :--- |
| `GET` | `/login` | Hiển thị form đăng nhập | Công khai |
| `POST` | `/login` | Xử lý đăng nhập bằng Spring Security | Công khai |
| `POST` | `/logout` | Xử lý đăng xuất và hủy session | Đã đăng nhập |
| `GET` | `/register` | Hiển thị form đăng ký tài khoản | Công khai |
| `POST` | `/register` | Xử lý đăng ký và gửi mã OTP | Công khai |
| `GET` | `/verify-otp` | Hiển thị form nhập OTP kích hoạt | Công khai |
| `POST` | `/verify-otp` | Kiểm tra OTP và kích hoạt tài khoản | Công khai |
| `POST` | `/resend-register-otp` | Gửi lại mã OTP kích hoạt | Công khai |
| `GET` | `/forgot-password` | Hiển thị form yêu cầu quên mật khẩu | Công khai |
| `POST` | `/forgot-password` | Xử lý gửi OTP đặt lại mật khẩu | Công khai |
| `GET` | `/reset-password` | Hiển thị form nhập OTP và mật khẩu mới | Công khai |
| `POST` | `/reset-password` | Xác thực OTP và cập nhật mật khẩu mới | Công khai |

### Nhóm Trang chủ & Dashboard
| Phương thức | URL | Mô tả chức năng | Quyền truy cập |
| :--- | :--- | :--- | :--- |
| `GET` | `/` | Trang chủ Dashboard (thống kê tổng số user & product) | Công khai |

### Nhóm Quản lý Sản phẩm (Product Management)
| Phương thức | URL | Mô tả chức năng | Quyền truy cập |
| :--- | :--- | :--- | :--- |
| `GET` | `/products` | Danh sách sản phẩm, tìm kiếm, phân trang | Đã đăng nhập |
| `GET` | `/products/create` | Form thêm sản phẩm mới | Đã đăng nhập |
| `POST` | `/products/create` | Lưu sản phẩm mới kèm upload ảnh | Đã đăng nhập |
| `GET` | `/products/edit/{id}` | Form chỉnh sửa sản phẩm | Đã đăng nhập |
| `POST` | `/products/edit/{id}` | Cập nhật thông tin sản phẩm và ảnh | Đã đăng nhập |
| `POST` | `/products/delete/{id}`| Xóa sản phẩm và dọn dẹp ảnh | Đã đăng nhập |

### Nhóm Quản lý Người dùng (User Management - Admin)
| Phương thức | URL | Mô tả chức năng | Quyền truy cập |
| :--- | :--- | :--- | :--- |
| `GET` | `/users` | Danh sách người dùng, tìm kiếm, phân trang | `ROLE_ADMIN` |
| `GET` | `/users/create` | Form thêm người dùng mới | `ROLE_ADMIN` |
| `POST` | `/users/create` | Lưu người dùng mới | `ROLE_ADMIN` |
| `GET` | `/users/edit/{id}` | Form cập nhật thông tin người dùng | `ROLE_ADMIN` |
| `POST` | `/users/edit/{id}` | Lưu cập nhật người dùng | `ROLE_ADMIN` |
| `POST` | `/users/delete/{id}` | Xóa người dùng khỏi hệ thống | `ROLE_ADMIN` |

---

## 7. HƯỚNG DẪN CÀI ĐẶT & CẤU HÌNH

### Bước 1: Yêu cầu môi trường
- Cài đặt **Java JDK 21** trở lên.
- Cài đặt **Microsoft SQL Server** (bản Developer hoặc Express).
- Cài đặt **Apache Maven 3.9+** (hoặc sử dụng Maven tích hợp sẵn trong IDE).

### Bước 2: Chuẩn bị Cơ sở dữ liệu
Khởi động SQL Server và mở SQL Server Management Studio (SSMS) hoặc Azure Data Studio, chạy câu lệnh tạo CSDL:
```sql
CREATE DATABASE webst3;
GO
```

### Bước 3: Cấu hình biến môi trường (`.env`)
Tạo hoặc kiểm tra file `.env` tại thư mục gốc của dự án (`BT09_2809/.env`):
```properties
# ===================================================
# DATABASE CONFIGURATION (SQL SERVER)
# ===================================================
DB_URL=jdbc:sqlserver://localhost:64078;databaseName=webst3;encrypt=false;trustServerCertificate=true;sslProtocol=TLSv1.2;characterEncoding=UTF-8
DB_USERNAME=sa
DB_PASSWORD=123456

# ===================================================
# JPA & HIBERNATE CONFIGURATION
# ===================================================
DDL_AUTO=update
SHOW_SQL=false

# ===================================================
# SERVER CONFIGURATION
# ===================================================
SERVER_PORT=8080

# ===================================================
# GMAIL SMTP CONFIGURATION
# ===================================================
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password

# ===================================================
# CLOUDINARY CONFIGURATION (OPTIONAL)
# ===================================================
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret
```

> **Lưu ý về cổng kết nối SQL Server**:
> Nếu SQL Server của bạn chạy ở cổng mặc định `1433`, hãy điều chỉnh `DB_URL` thành:  
> `jdbc:sqlserver://localhost:1433;databaseName=webst3;...`

### Bước 4: Biên dịch và Khởi chạy ứng dụng
Mở terminal tại thư mục gốc của dự án:
```powershell
# Biên dịch dự án và sinh mã MapStruct
mvn clean test-compile

# Khởi chạy Spring Boot
mvn spring-boot:run
```

Khi màn hình console hiển thị `Started Bt092809Application in ... seconds`, ứng dụng đã sẵn sàng tại địa chỉ:  
👉 **`http://localhost:8080`**

---

## 8. QUY TRÌNH & KỊCH BẢN KIỂM THỬ CHI TIẾT

### Kịch bản 1: Kiểm tra Đăng nhập & Phân quyền
1. Truy cập `http://localhost:8080/login`.
2. Đăng nhập bằng tài khoản Admin: `admin` / `123456` (hoặc email `admin@gmail.com`).
   - Kiểm tra Header: Hiển thị tên `Administrator`, badge `[ROLE_ADMIN]`, xuất hiện đầy đủ 3 menu: `Dashboard`, `Products`, `Users`.
3. Nhấn **Logout**.
4. Đăng nhập bằng tài khoản User: `user01` / `123456` (hoặc email `user01@gmail.com`).
   - Kiểm tra Header: Hiển thị tên `Đỗ Thanh Thành Tài`, badge `[ROLE_USER]`, chỉ xuất hiện menu: `Dashboard`, `Products` (menu `Users` được ẩn tự động).
   - Thử cố tình truy cập vào `http://localhost:8080/users` -> Hệ thống chặn và trả về mã lỗi `403 Forbidden` hoặc chuyển hướng an toàn.

---

### Kịch bản 2: Kiểm tra Đăng ký tài khoản & Xác thực OTP
1. Truy cập `http://localhost:8080/register`.
2. Nhập thông tin:
   - Họ tên: `Nguyễn Văn A`
   - Username: `nguyenvana`
   - Email: `nguyenvana@gmail.com`
   - Mật khẩu & Nhập lại mật khẩu: `123456`
3. Nhấn nút **Đăng ký & nhận OTP**.
4. Hệ thống chuyển hướng sang màn hình `/verify-otp?email=nguyenvana@gmail.com`.
5. Mở cửa sổ Terminal/Console để xem mã OTP hiển thị:
   ```text
   >> [OTP LOCAL LOG] Email: nguyenvana@gmail.com | OTP: 123456
   ```
6. Nhập mã OTP 6 số vào form và nhấn **Xác nhận kích hoạt**.
7. Hệ thống thông báo kích hoạt thành công và chuyển về trang Đăng nhập.
8. Thử đăng nhập bằng tài khoản `nguyenvana` vừa tạo -> Đăng nhập thành công!

---

### Kịch bản 3: Kiểm tra Quên mật khẩu & Đổi mật khẩu mới
1. Truy cập `http://localhost:8080/forgot-password`.
2. Nhập email: `nguyenvana@gmail.com` và nhấn **Gửi mã OTP**.
3. Hệ thống chuyển hướng sang trang `/reset-password?email=nguyenvana@gmail.com`.
4. Xem mã OTP trong terminal console.
5. Nhập mã OTP, mật khẩu mới `654321` và xác nhận mật khẩu `654321`.
6. Nhấn **Đổi mật khẩu** -> Hệ thống thông báo thành công.
7. Đăng nhập lại với mật khẩu mới `654321` -> Đăng nhập thành công!

---

### Kịch bản 4: Kiểm tra Quản lý Sản phẩm, Phân trang & Modal Xóa
1. Đăng nhập vào hệ thống và vào menu **Products** (`http://localhost:8080/products`).
2. **Kiểm tra Phân trang**:
   - Hệ thống đã nạp sẵn 20 sản phẩm mẫu (iPhone 16, Samsung S24, MacBook, PS5, Switch,...).
   - Bấm vào số trang **1**, **2**, **«**, **»** để kiểm tra chuyển trang mượt mà.
3. **Kiểm tra Tìm kiếm**:
   - Nhập từ khóa `samsung` hoặc `macbook` vào ô tìm kiếm và nhấn **Tìm kiếm**.
   - Bảng lọc chính xác các sản phẩm tương ứng.
4. **Kiểm tra Thêm mới kèm Upload ảnh**:
   - Nhấn nút **+ Thêm Product**.
   - Nhập tên: `Bàn phím cơ Custom`, giá: `3500000`, mô tả và chọn một file ảnh từ máy tính.
   - Nhấn **Lưu** -> Sản phẩm hiển thị ngay trên danh sách kèm ảnh đại diện rõ nét và giá hiển thị chuẩn `3,500,000`.
5. **Kiểm tra Modal Xác nhận xóa**:
   - Nhấn nút **Delete** tại một sản phẩm bất kỳ.
   - Hộp thoại Modal hiện lên giữa màn hình kèm cảnh báo: `Bạn có chắc chắn muốn xóa sản phẩm "..." không?`
   - Bấm **Hủy** (hoặc bấm `Esc`) -> Modal đóng lại, sản phẩm không bị xóa.
   - Bấm **Delete** lại và chọn **Xác nhận xóa** -> Sản phẩm được xóa ngay lập tức và có thông báo thành công.

---

### Kịch bản 5: Kiểm tra Quản trị Người dùng (Dành cho Admin)
1. Đăng nhập bằng tài khoản `admin`.
2. Vào menu **Users** (`http://localhost:8080/users`).
3. Kiểm tra danh sách người dùng:
   - Cột **Products** hiển thị chính xác số lượng sản phẩm mà mỗi user sở hữu.
   - Cột **Role** hiển thị `ROLE_ADMIN` hoặc `ROLE_USER`.
4. Thử chỉnh sửa thông tin hoặc thêm mới User.
5. Kiểm tra tính năng xóa người dùng qua Modal xác nhận.

---

## 9. CÁC ĐIỂM NỔI BẬT & TỐI ƯU KỸ THUẬT

1. **Bảo mật OTP cấp độ cao**:
   - Không lưu OTP dạng plaintext.
   - Sử dụng `BCryptPasswordEncoder` để so khớp (`matches()`).
   - Kiểm soát chặt chẽ `expiresAt` (5 phút) và `attempts` (tối đa 5 lần).
2. **Khắc phục lỗi N+1 Query & Lazy Loading**:
   - Sử dụng `join fetch p.user` trong `ProductRepository` giúp lấy toàn bộ thông tin sản phẩm và người sở hữu chỉ trong **1 câu truy vấn SQL duy nhất**, ngăn ngừa hoàn toàn lỗi `LazyInitializationException`.
3. **Cơ chế Upload ảnh hai tầng (Dual-Layer Storage)**:
   - Tích hợp chuẩn Cloudinary SDK.
   - Có cơ chế Fallback tự động lưu file về thư mục cục bộ `uploads/` và cấu hình `ResourceHandler` để phục vụ file qua `/uploads/**`, đảm bảo hệ thống không bao giờ bị gián đoạn hoạt động ngay cả khi mất kết nối Internet hoặc chưa cấu hình API Key Cloudinary.
4. **Trải nghiệm người dùng (UX/UI)**:
   - Giao diện thiết kế theo phong cách hiện đại, thanh lịch (Modern Clean Design).
   - Định dạng số tiền tệ tự nhiên, không gây nhầm lẫn.
   - Toàn bộ thao tác xóa quan trọng đều có Modal Dialog bảo vệ trực quan.

---

*Báo cáo được hoàn thiện và kiểm thử thành công trên môi trường Spring Boot 4 & SQL Server.*
