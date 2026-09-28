# Bài Tập 09: Spring Boot 4 + Spring Security + MapStruct + Thymeleaf

## Chức năng Custom Login (Username hoặc Email)
- Sử dụng Spring Boot 4.1.1, Spring Security, MapStruct 1.6.3, Thymeleaf Layout Dialect (`nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect`).
- Cho phép người dùng đăng nhập bằng **Username** hoặc **Email** đều được.
- Custom User Details: Cung cấp `fullName`, `images`, `email`, `role`, `username` ra view.
- Hiển thị đầy đủ ở `header.html`:
  - Ảnh đại diện đại diện (Avatar: `/images/user.png` hoặc `/images/avatar-default.png`)
  - Họ và tên (`fullName`)
  - Username (`(username)`)
  - Email
  - Vai trò (`ROLE_ADMIN` / `ROLE_USER`)
  - Nút Đăng xuất (Logout)

### Tài khoản kiểm thử:
1. **Admin**:
   - Username: `thanhtai`
   - Email: `thanhtai@hcmute.edu.vn`
   - Password: `123456`
   - Role: `ROLE_ADMIN`
2. **User**:
   - Username: `user01`
   - Email: `user01@gmail.com`
   - Password: `123456`
   - Role: `ROLE_USER`

### Hướng dẫn chạy:
```bash
mvn spring-boot:run
```
Truy cập: `http://localhost:8080/login`
