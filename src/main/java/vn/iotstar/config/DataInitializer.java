package vn.iotstar.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.iotstar.entity.Product;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.repository.ProductRepository;
import vn.iotstar.repository.RoleRepository;
import vn.iotstar.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(
            RoleRepository roleRepository,
            UserRepository userRepository,
            ProductRepository productRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            Role userRole = roleRepository
                    .findByName("ROLE_USER")
                    .orElseGet(() -> roleRepository.save(
                            Role.builder()
                                    .name("ROLE_USER")
                                    .build()
                    ));

            Role adminRole = roleRepository
                    .findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(
                            Role.builder()
                                    .name("ROLE_ADMIN")
                                    .build()
                    ));

            User user01 = userRepository.findByUsername("user01").map(u -> {
                u.setFullName("Đỗ Thanh Thành Tài");
                return userRepository.save(u);
            }).orElseGet(() -> {
                User user = User.builder()
                        .username("user01")
                        .email("user01@gmail.com")
                        .password(passwordEncoder.encode("123456"))
                        .fullName("Đỗ Thanh Thành Tài")
                        .role(userRole)
                        .enabled(true)
                        .build();
                return userRepository.save(user);
            });

            if (userRepository.findByUsername("admin").isEmpty() && userRepository.findByEmail("admin@gmail.com").isEmpty()) {
                User admin = User.builder()
                        .username("admin")
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("123456"))
                        .fullName("Administrator")
                        .role(adminRole)
                        .enabled(true)
                        .build();
                userRepository.save(admin);
            }

            // Seed sample products if count < 10 for pagination testing
            if (productRepository.count() < 10) {
                List<Product> sampleProducts = List.of(
                        Product.builder()
                                .name("iPhone 16 Pro Max 256GB Titan Sa Mạc")
                                .description("Điện thoại Apple cao cấp nhất với chip A18 Pro và camera 48MP")
                                .price(new BigDecimal("34990000"))
                                .imageUrl("https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=300")
                                .user(user01)
                                .build(),
                        Product.builder()
                                .name("iPhone 15 Pro Max 256GB Titan Tự Nhiên")
                                .description("Khung viền titan siêu nhẹ, Action Button, camera tiềm vọng 5x")
                                .price(new BigDecimal("29490000"))
                                .imageUrl("https://images.unsplash.com/photo-1592750475338-74b7b21085ab?w=300")
                                .user(user01)
                                .build(),
                        Product.builder()
                                .name("Điện thoại Samsung Galaxy S24 Ultra")
                                .description("Galaxy AI thông minh vượt trội, bút S Pen và khung Titan")
                                .price(new BigDecimal("26000000"))
                                .imageUrl("https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=300")
                                .user(user01)
                                .build(),
                        Product.builder()
                                .name("Samsung Galaxy Z Fold6 512GB")
                                .description("Màn hình gập thế hệ mới, mỏng nhẹ hơn, tích hợp Galaxy AI")
                                .price(new BigDecimal("41990000"))
                                .imageUrl("https://images.unsplash.com/photo-1580910051074-3eb694886505?w=300")
                                .user(user01)
                                .build(),
                        Product.builder()
                                .name("Laptop MacBook Pro 14 M3 Pro (18GB/512GB)")
                                .description("Hiệu năng đỉnh cao cho lập trình viên và thiết kế đồ họa chuyên nghiệp")
                                .price(new BigDecimal("49990000"))
                                .imageUrl("https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=300")
                                .user(user01)
                                .build(),
                        Product.builder()
                                .name("Laptop Dell XPS 15 9530 Core i7 RTX 4060")
                                .description("Màn hình OLED 3.5K sắc nét, thiết kế viền siêu mỏng sang trọng")
                                .price(new BigDecimal("45000000"))
                                .imageUrl("https://images.unsplash.com/photo-1593642632823-8f785ba67e45?w=300")
                                .user(user01)
                                .build(),
                        Product.builder()
                                .name("Laptop Gaming Asus ROG Zephyrus G16")
                                .description("Intel Core Ultra 9, RTX 4070, màn hình OLED 240Hz siêu mượt")
                                .price(new BigDecimal("38500000"))
                                .imageUrl("https://images.unsplash.com/photo-1603302576837-37561b2e2302?w=300")
                                .user(user01)
                                .build(),
                        Product.builder()
                                .name("Tai nghe chống ồn Sony WH-1000XM5")
                                .description("Chống ồn hàng đầu thế giới, chất âm Hi-Res Audio đỉnh cao")
                                .price(new BigDecimal("7990000"))
                                .imageUrl("https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=300")
                                .user(user01)
                                .build(),
                        Product.builder()
                                .name("Tai nghe không dây Apple AirPods Pro 2 USB-C")
                                .description("Chống ồn chủ động gấp đôi, âm thanh thích ứng thế hệ mới")
                                .price(new BigDecimal("5690000"))
                                .imageUrl("https://images.unsplash.com/photo-1600294037681-c80b4cb5b434?w=300")
                                .user(user01)
                                .build(),
                        Product.builder()
                                .name("Đồng hồ thông minh Apple Watch Ultra 2")
                                .description("Vỏ titan 49mm, định vị GPS kép chuẩn xác, pin trâu đến 72 giờ")
                                .price(new BigDecimal("21490000"))
                                .imageUrl("https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=300")
                                .user(user01)
                                .build(),
                        Product.builder()
                                .name("Đồng hồ Samsung Galaxy Watch 6 Classic")
                                .description("Vòng xoay bezel vật lý đặc trưng, theo dõi sức khỏe toàn diện")
                                .price(new BigDecimal("6990000"))
                                .imageUrl("https://images.unsplash.com/photo-1508685096489-7aacd43bd3b1?w=300")
                                .user(user01)
                                .build(),
                        Product.builder()
                                .name("Máy tính bảng iPad Pro 11 inch M4 256GB")
                                .description("Màn hình Ultra Retina XDR OLED, siêu mỏng 5.3mm với chip Apple M4")
                                .price(new BigDecimal("28990000"))
                                .imageUrl("https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=300")
                                .user(user01)
                                .build(),
                        Product.builder()
                                .name("Máy tính bảng iPad Air 6 M2 128GB")
                                .description("Hiệu năng mạnh mẽ với vi xử lý Apple M2, hỗ trợ Apple Pencil Pro")
                                .price(new BigDecimal("16490000"))
                                .imageUrl("https://images.unsplash.com/photo-1561154464-82e9adf32764?w=300")
                                .user(user01)
                                .build(),
                        Product.builder()
                                .name("Bàn phím cơ không dây Keychron Q1 Pro")
                                .description("Khung nhôm CNC nguyên khối, switch cơ học gõ êm, kết nối Bluetooth")
                                .price(new BigDecimal("4500000"))
                                .imageUrl("https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=300")
                                .user(user01)
                                .build(),
                        Product.builder()
                                .name("Chuột không dây Logitech MX Master 3S")
                                .description("Cảm biến 8K DPI trên mọi bề mặt, con lăn MagSpeed siêu nhanh")
                                .price(new BigDecimal("2290000"))
                                .imageUrl("https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=300")
                                .user(user01)
                                .build(),
                        Product.builder()
                                .name("Màn hình đồ họa LG UltraFine 27 inch 4K IPS")
                                .description("Độ phân giải 4K HDR400, chuẩn màu 99% sRGB, kết nối USB Type-C 90W")
                                .price(new BigDecimal("12990000"))
                                .imageUrl("https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=300")
                                .user(user01)
                                .build(),
                        Product.builder()
                                .name("Loa Bluetooth Marshall Stanmore III")
                                .description("Âm thanh stereo sống động, thiết kế vintage biểu tượng đậm chất rock")
                                .price(new BigDecimal("8990000"))
                                .imageUrl("https://images.unsplash.com/photo-1545454675-3531b543be5d?w=300")
                                .user(user01)
                                .build(),
                        Product.builder()
                                .name("Máy chơi game Sony PlayStation 5 Slim Standard")
                                .description("Ổ SSD 1TB siêu tốc, hỗ trợ đồ họa 4K 120Hz và Ray Tracing chân thực")
                                .price(new BigDecimal("13990000"))
                                .imageUrl("https://images.unsplash.com/photo-1606813907291-d86efa9b94db?w=300")
                                .user(user01)
                                .build(),
                        Product.builder()
                                .name("Máy chơi game Nintendo Switch OLED Model")
                                .description("Màn hình OLED 7 inch rực rỡ, chân đế điều chỉnh linh hoạt")
                                .price(new BigDecimal("7490000"))
                                .imageUrl("https://images.unsplash.com/photo-1578303512597-81e6cc155b3e?w=300")
                                .user(user01)
                                .build(),
                        Product.builder()
                                .name("Flycam DJI Mini 4 Pro Fly More Combo Plus")
                                .description("Quay 4K 60fps HDR, cảm biến va chạm đa hướng, thời gian bay 45 phút")
                                .price(new BigDecimal("21990000"))
                                .imageUrl("https://images.unsplash.com/photo-1508614589041-895b88991e3e?w=300")
                                .user(user01)
                                .build()
                );
                productRepository.saveAll(sampleProducts);
            }
        };
    }
}
