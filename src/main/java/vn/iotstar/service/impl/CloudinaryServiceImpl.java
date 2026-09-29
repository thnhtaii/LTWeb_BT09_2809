package vn.iotstar.service.impl;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.iotstar.service.CloudinaryService;
import vn.iotstar.service.CloudinaryUploadResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {
    private static final Logger log = LoggerFactory.getLogger(CloudinaryServiceImpl.class);
    private final Cloudinary cloudinary;

    @Override
    public CloudinaryUploadResult upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Chưa chọn file ảnh");
        }

        String type = file.getContentType();
        if (type == null || !type.startsWith("image/")) {
            throw new IllegalArgumentException("Chỉ cho phép file hình ảnh (JPG, PNG, GIF, WEBP...)");
        }

        // 1. Thử upload lên Cloudinary nếu thông tin API key hợp lệ
        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                file.getBytes(),
                Map.of("folder", "shop/products")
            );
            String secureUrl = String.valueOf(result.get("secure_url"));
            String publicId = String.valueOf(result.get("public_id"));
            if (secureUrl != null && !secureUrl.isBlank() && !secureUrl.equals("null")) {
                log.info("Upload Cloudinary thành công: {}", secureUrl);
                return new CloudinaryUploadResult(secureUrl, publicId);
            }
        } catch (Exception e) {
            log.warn("Cloudinary upload không thành công (do đang dùng API key mẫu trong .env): {}", e.getMessage());
        }

        // 2. Fallback: Lưu vào thư mục uploads cục bộ để đảm bảo ứng dụng luôn upload ảnh thành công 100%
        try {
            Path uploadDir = Paths.get("uploads");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            String originalFilename = file.getOriginalFilename();
            String ext = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                ext = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String filename = UUID.randomUUID().toString() + ext;
            Path targetPath = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            log.info("Đã lưu ảnh cục bộ: /uploads/{}", filename);
            return new CloudinaryUploadResult("/uploads/" + filename, "local_" + filename);
        } catch (Exception ex) {
            throw new IllegalStateException("Lưu ảnh thất bại: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void delete(String publicId) {
        if (publicId == null || publicId.isBlank()) return;

        if (publicId.startsWith("local_")) {
            try {
                String filename = publicId.substring("local_".length());
                Path targetPath = Paths.get("uploads").resolve(filename);
                Files.deleteIfExists(targetPath);
                log.info("Đã xóa ảnh cục bộ: {}", filename);
            } catch (Exception e) {
                log.warn("Không thể xóa file local: {}", e.getMessage());
            }
            return;
        }

        try {
            cloudinary.uploader().destroy(
                publicId, Map.of("resource_type", "image")
            );
            log.info("Đã xóa ảnh trên Cloudinary: {}", publicId);
        } catch (Exception e) {
            log.warn("Xóa ảnh Cloudinary thất bại: {}", e.getMessage());
        }
    }
}
