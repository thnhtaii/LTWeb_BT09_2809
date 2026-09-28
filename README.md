# BÁO CÁO BÀI TẬP 09: SPRING BOOT 4 + SPRING SECURITY + MAPSTRUCT + THYMELEAF


---

## 1. Yêu cầu và Kết quả hoàn thành

| STT | Yêu cầu bài tập | Chi tiết hiện thực |
| :-: | :--- | :--- |
| **01** | **Bảng User và Role** | Thiết kế Entity `User` và `Role` chuẩn quan hệ nhiều-một (`@ManyToOne`), tự động ánh xạ bảng trong SQL Server qua Spring Data JPA. |
| **02** | **Chức năng Login** | Cấu hình form login bằng Spring Security 6/7, tích hợp xác thực và mã hóa mật khẩu an toàn với `BCryptPasswordEncoder`. |
| **03** | **Custom Login (Username hoặc Email)** | Cho phép người dùng đăng nhập linh hoạt bằng **Username** hoặc **Email**; hệ thống tự động tìm kiếm qua `findByUsernameOrEmail(login, login)`. |
| **04** | **Hiển thị thông tin User trên Header** | `header.html` hiển thị đầy đủ: **Ảnh đại diện** (Avatar), **Họ và tên** (`fullName`), **Username**, **Email**, **Vai trò** (`role`), cùng nút **Đăng xuất**. |
| **05** | **Sử dụng MapStruct** | Khai báo `UserMapper` tự động chuyển đổi giữa `User` (Entity) và `UserDTO` với cấu hình `mapstruct-processor` 1.6.3 trong Maven. |
| **06** | **Thymeleaf & Thymeleaf Layout Dialect** | Kế thừa layout chuẩn qua `thymeleaf-layout-dialect` bằng thuộc tính `layout:decorate="~{layouts/layout}"`. |
| **07** | **Bảo mật và Phân quyền (Authorization)** | Phân quyền chi tiết: tài nguyên tĩnh và trang login công khai; `/admin/**` yêu cầu role `ADMIN`; trang người dùng yêu cầu xác thực (`authenticated`). |

---

## 2. Công nghệ sử dụng
- **Java**: 21 (hoặc 26)
- **Spring Boot**: 4.1.1
- **Spring Security**: 6.x / 7.x
- **Spring Data JPA & Hibernate**: Quản lý Entity và CSDL
- **Microsoft SQL Server**: Lưu trữ dữ liệu quan hệ (`mssql-jdbc`)
- **MapStruct**: 1.6.3 (Ánh xạ DTO - Entity)
- **Thymeleaf**: Template engine kết hợp `thymeleaf-layout-dialect` và `thymeleaf-extras-springsecurity6`
- **Lombok**: Giảm thiểu boilerplate code
- **CSS**: Vanilla CSS hiện đại, responsive

---

## 3. Cấu trúc dự án

```text
BT09_2809/
├── .env                                       # Cấu hình biến môi trường kết nối CSDL và Server
├── .gitignore                                 # Loại trừ target/, .env và các file tạm
├── pom.xml                                    # Khai báo dependency và compiler plugin
├── README.md                                  # Hướng dẫn chi tiết dự án
└── src/
    └── main/
        ├── java/
        │   └── vn/iotstar/
        │       ├── Bt092809Application.java  # Lớp khởi chạy Spring Boot
        │       ├── config/
        │       │   ├── DataInitializer.java   # Khởi tạo Role (ROLE_ADMIN, ROLE_USER) và User mẫu
        │       │   ├── EncodingConfig.java    # Bộ lọc ký tự tiếng Việt UTF-8
        │       │   └── SecurityConfig.java    # Cấu hình SecurityFilterChain, Provider, PasswordEncoder
        │       ├── controller/
        │       │   ├── AuthController.java    # Điều hướng /login, /access-denied
        │       │   └── HomeController.java    # Điều hướng trang chủ /
        │       ├── dto/
        │       │   ├── LoginDTO.java          # DTO nhận dữ liệu đăng nhập
        │       │   └── UserDTO.java           # DTO trao đổi thông tin người dùng
        │       ├── entity/
        │       │   ├── Role.java              # Entity bảng roles
        │       │   ├── User.java              # Entity bảng users
        │       │   └── Product.java           # Entity bảng products
        │       ├── mapper/
        │       │   └── UserMapper.java        # Interface MapStruct chuyển đổi User <-> UserDTO
        │       ├── repository/
        │       │   ├── RoleRepository.java    # Truy vấn dữ liệu Role
        │       │   └── UserRepository.java    # Truy vấn dữ liệu User (findByUsernameOrEmail)
        │       ├── security/
        │       │   ├── CustomUserDetails.java # Đối tượng Principal chứa thông tin mở rộng của User
        │       │   └── CustomUserDetailsService.java # Nạp thông tin xác thực từ DB
        │       └── service/
        │           ├── AuthService.java       # Interface nghiệp vụ đăng ký/xác thực
        │           └── impl/
        │               └── AuthServiceImpl.java
        └── resources/
            ├── application.properties         # Cấu hình Spring Boot và import .env
            ├── static/
            │   ├── css/
            │   │   └── app.css                # Giao diện responsive cho UTEShop
            │   └── images/
            │       ├── avatar-default.png     # Ảnh đại diện mặc định
            │       └── user.png               # Ảnh người dùng
            └── templates/
                ├── auth/
                │   ├── login.html             # Trang đăng nhập
                │   └── 403.html               # Trang báo lỗi 403 (Truy cập bị từ chối)
                ├── fragments/
                │   └── header.html            # Fragment thanh điều hướng hiển thị Avatar, Info, Logout
                ├── layouts/
                │   └── layout.html            # Layout chung sử dụng Thymeleaf Layout Dialect
                └── home.html                  # Trang chủ kế thừa layout:decorate
```

---

## 4. Cấu hình Cơ sở dữ liệu (Database)

Ứng dụng kết nối tới Microsoft SQL Server (database `webst4`):
- **File `.env`**:
  ```properties
  DB_URL=jdbc:sqlserver://localhost:64078;databaseName=webst4;encrypt=false;trustServerCertificate=true;sslProtocol=TLSv1.2;characterEncoding=UTF-8
  DB_USERNAME=sa
  DB_PASSWORD=13234
  DDL_AUTO=update
  SHOW_SQL=true
  SERVER_PORT=8080
  ```
- Khi ứng dụng khởi động, Hibernate sẽ tự động cập nhật bảng `users` và `roles` trong CSDL.

---

## 5. Danh sách tài khoản kiểm thử đã tạo sẵn

Cả 2 tài khoản dưới đây đều có thể đăng nhập bằng **Username** hoặc **Email**:

| Vai trò (Role) | Username | Email | Mật khẩu | Họ và tên (`fullName`) | Ảnh đại diện |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **ROLE_ADMIN** | `thanhtai` | `thanhtai@hcmute.edu.vn` | `123456` | Đỗ Thành Thanh Tài | `/images/user.png` |
| **ROLE_USER** | `user01` | `user01@gmail.com` | `123456` | Nguyễn Hữu Trung | `/images/user.png` |

---

## 6. Hướng dẫn chạy và kiểm thử

### Bước 1: Khởi chạy dự án
Chạy bằng Maven qua terminal:
```bash
mvn spring-boot:run
```
Hoặc mở trong **Spring Tool Suite (STS) / Eclipse**: Chuột phải vào project `BT09_2809` -> chọn `Run As` -> `Spring Boot App`.

### Bước 2: Kiểm thử đăng nhập
Truy cập: **`http://localhost:8080/login`**

1. **Trường hợp 1 - Đăng nhập Admin bằng Username**:
   - Username hoặc Email: `thanhtai`
   - Password: `123456`
   - *Kết quả*: Đăng nhập thành công, chuyển hướng về trang chủ `/`. Thanh `header.html` hiển thị ảnh đại diện, `Đỗ Thành Thanh Tài`, `(thanhtai)`, `thanhtai@hcmute.edu.vn`, vai trò `ROLE_ADMIN` và nút Đăng xuất.

2. **Trường hợp 2 - Đăng nhập Admin bằng Email**:
   - Username hoặc Email: `thanhtai@hcmute.edu.vn`
   - Password: `123456`
   - *Kết quả*: Đăng nhập thành công với đầy đủ thông tin trên header.

3. **Trường hợp 3 - Đăng nhập User thông thường bằng Username hoặc Email**:
   - Nhập `user01` hoặc `user01@gmail.com` với password `123456`.
   - *Kết quả*: Đăng nhập thành công, hiển thị vai trò `ROLE_USER` trên header.

4. **Trường hợp 4 - Đăng nhập sai thông tin**:
   - Nhập sai mật khẩu hoặc tài khoản không tồn tại.
   - *Kết quả*: Trang web thông báo lỗi `Username/email hoặc password không đúng`.

5. **Trường hợp 5 - Đăng xuất**:
   - Bấm nút **Đăng xuất** trên thanh Header.
   - *Kết quả*: Phiên làm việc (Session) bị hủy, cookie `JSESSIONID` bị xóa, chuyển hướng về trang login kèm thông báo `Bạn đã đăng xuất`.
