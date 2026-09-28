# Bài Tập 09: Spring Boot 4 + Spring Security + MapStruct + Thymeleaf

## Ví dụ 1: Chức năng Login bằng Spring Security
- Sử dụng Spring Boot 4.1.1, Spring Security, MapStruct 1.6.3, Thymeleaf (layout không dùng Dialect).
- Xác thực người dùng qua database SQL Server (bảng `users` và `roles`).
- Hiển thị thông tin người dùng đã đăng nhập ở `header.html` (`sec:authorize="isAuthenticated()"` và `sec:authentication="name"`).
- Phân quyền theo vai trò (`ADMIN`, `USER`).

### Tài khoản mặc định:
1. **Admin**:
   - Email: `trungnh@hcmute.edu.vn`
   - Password: `123456`
   - Role: `ADMIN`
2. **User**:
   - Email: `user01@gmail.com`
   - Password: `123456`
   - Role: `USER`

### Hướng dẫn chạy:
```bash
mvn spring-boot:run
```
Truy cập: `http://localhost:8080/login`
