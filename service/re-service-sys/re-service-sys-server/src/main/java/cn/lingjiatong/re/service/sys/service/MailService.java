package cn.lingjiatong.re.service.sys.service;

import cn.lingjiatong.re.common.exception.BusinessException;
import cn.lingjiatong.re.common.exception.ErrorEnum;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * 邮件发送service层
 *
 * @author Ling, Jiatong
 */
@Slf4j
@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * 发送邮箱验证码
     *
     * @param to 收件人邮箱
     * @param scene 场景描述，如 绑定邮箱 / 修改密码
     * @param code 验证码
     */
    public void sendVerifyCode(String to, String scene, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("【根元素博客】邮箱验证码");
            String content = "<div style=\"font-family: 'Microsoft YaHei', sans-serif; padding: 20px;\">" +
                    "<h3 style=\"color: #303133;\">" + scene + "验证码</h3>" +
                    "<p style=\"color: #606266; font-size: 14px;\">您正在进行<span style=\"color: #409eff;\">" + scene +
                    "</span>操作，验证码为：</p>" +
                    "<p style=\"font-size: 28px; font-weight: bold; color: #409eff; letter-spacing: 4px;\">" + code + "</p>" +
                    "<p style=\"color: #909399; font-size: 12px;\">验证码 5 分钟内有效，若非本人操作请忽略本邮件。</p>" +
                    "</div>";
            helper.setText(content, true);
            mailSender.send(message);
            log.info("验证码邮件发送成功, to: {}, scene: {}", to, scene);
        } catch (Exception e) {
            log.error("验证码邮件发送失败, to: {}, scene: {}", to, scene, e);
            throw new BusinessException(ErrorEnum.EMAIL_SEND_FAIL_ERROR.getCode(), "验证码邮件发送失败，请稍后重试");
        }
    }
}
