package vn.iotstar.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import vn.iotstar.service.EmailService;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final JavaMailSender mailSender;

    @Override
    public void sendOtp(String email, String otp, String subject) {
        // In ra console mã OTP để thuận tiện kiểm thử local ngay cả khi chưa cấu hình Gmail App Password
        System.out.println("=================================================");
        System.out.println(">> [OTP LOCAL LOG] Email: " + email + " | OTP: " + otp);
        System.out.println("=================================================");

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject(subject);
            message.setText("""
Xin chào,

Mã OTP của bạn là: %s

OTP có hiệu lực trong 5 phút và chỉ sử dụng một lần.
Không chia sẻ mã này cho người khác.
""".formatted(otp));
            mailSender.send(message);
            log.info("Đã gửi email OTP thành công tới {}", email);
        } catch (Exception e) {
            log.warn("Không thể gửi email OTP qua SMTP (vui lòng kiểm tra MAIL_USERNAME / MAIL_PASSWORD trong .env nếu muốn gửi thật): {}", e.getMessage());
        }
    }
}
