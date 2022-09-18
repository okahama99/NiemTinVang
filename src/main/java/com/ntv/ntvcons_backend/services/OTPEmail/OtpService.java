package com.ntv.ntvcons_backend.services.OTPEmail;

import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.entities.User;
import com.ntv.ntvcons_backend.repositories.UserRepository;
import com.ntv.ntvcons_backend.services.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OtpService {
    private OtpGenerator otpGenerator;
    private EmailService emailService;
    private UserService userService;
    private JavaMailSender mailSender;

    @Autowired
    UserRepository userRepository;

    private final List<Status> N_D_S_STATUS_LIST = Status.getAllNonDefaultSearchStatus();

    public Boolean generateOTPForResetPassword(String email) {
        // generate otp
        Integer otpValue = otpGenerator.generateOTP(email);
        if (otpValue == -1)
        {
            return  false;
        }
        User user = userRepository.findByEmailAndStatusNotIn(email, N_D_S_STATUS_LIST).orElse(null);
            // fetch user e-mail from database
            // generate emailDTO object
            emailService.send(email, buildSendOtpEmail(user.getFullName(), otpValue), "Lấy lại mật khẩu");
            // send generated e-mail
            return true;
    }

    public String generateVerificationOTP(String email) {
        // generate otp
        Integer otpValue = otpGenerator.generateOTP(email);
        if (otpValue == -1)
        {
            return null;
        }
        User user = userRepository.findByEmailAndStatusNotIn(email, N_D_S_STATUS_LIST).orElse(null);
        // fetch user e-mail from database
        // generate emailDTO object
        emailService.send(email, buildSendOtp(user.getFullName(), otpValue), "Niềm Tin Vàng OTP");
        // send generated e-mail
        return email;
    }

    public Integer generatePhoneOTP(String phone) {
        Integer otpValue = otpGenerator.generateOTP(phone);
        if (otpValue == -1)
        {
            return 0;
        }
        return otpValue;
    }
    /**
     * Method for validating provided OTP
     *
     * @param key - provided key
     * @param otpNumber - provided OTP number
     * @return boolean value (true|false)
     */
    public Boolean validateOTP(String key, Integer otpNumber)
    {
        // get OTP from cache
        Integer cacheOTP = otpGenerator.getOPTByKey(key);
        if (cacheOTP!=null && cacheOTP.equals(otpNumber))
        {
            otpGenerator.clearOTPFromCache(key);
            return true;
        }
        return false;
    }

    private String buildSendOtp(String name, int otp) {
        return  "Niềm Tin V&agrave;ng gửi qu&yacute; kh&aacute;ch " + name + " m&atilde; OTP : " + otp;
    }

    private String buildSendOtpEmail(String name, int otp) {
        return "<h1 style=\"color: #5e9ca0; text-align: center;\">Niềm Tin Vàng</h1>\n" +
                "<h3 style=\"color: #2e6c80; text-align: center;\">Thay đổi mật khẩu:</h3>\n" +
                "<p>Xin ch&agrave;o&nbsp;"+ name + "<br />Ch&uacute;ng t&ocirc;i l&agrave; team Niềm Tin Vàng,&nbsp;</p>\n" +
                "<p>Qu&yacute; kh&aacute;ch nhận được mail n&agrave;y l&agrave; v&igrave; qu&yacute; kh&aacute;ch (cũng c&oacute; thể l&agrave; ai đ&oacute; giả mạo danh nghĩa qu&yacute; kh&aacute;ch thực hiện thay đổi mật khẩu tr&ecirc;n website niemtinvang.vn, vui l&ograve;ng ghi lại m&atilde; OTP dưới đ&acirc;y để tiếp tục tạo mật khẩu mới.</p>\n" +
                "<p>"+ otp + "&nbsp;</p>\n" +
                "<p>M&atilde; OTP sẽ c&oacute; hiệu lực trong v&ograve;ng 5 ph&uacute;t.</p>\n" +
                "<p>Nếu đ&acirc;y kh&ocirc;ng phải do qu&yacute; kh&aacute;ch thực hiện thao t&aacute;c, c&oacute; thể y&ecirc;n t&acirc;m bỏ qua nội dung mail n&agrave;y.</p>\n" +
                "<p>Cảm ơn qu&yacute; kh&aacute;ch đ&atilde; sử dụng diễn đ&agrave;n về hoa của ch&uacute;ng t&ocirc;i, xin ch&uacute;c qu&yacute; kh&aacute;ch c&oacute; một ng&agrave;y tốt l&agrave;nh.</p>\n" +
                "<p>Đường dẫn tới website: niemtinvang.vn</p>\n" +
                "<p>Xin vui l&ograve;ng kh&ocirc;ng trả lời mail từ hệ thống.</p>\n" +
                "<p><strong>&nbsp;</strong></p>";
    }
}
