package com.neobis.rentit.utils;

import com.neobis.rentit.model.User;
import lombok.experimental.UtilityClass;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@UtilityClass
public class EmailUtility {

//    final String siteURL =
//            ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    private final String siteURL = "https://128.199.30.62:8080/";
    public void sendVerificationEmail(User user, JavaMailSender mailSender)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "renitt3test@gmail.com";
        String senderName = "Rent It";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Rent It.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getFirstName());
        String verifyURL = siteURL + "api/auth/verifyUser?code=" + user.getVerificationCode();
        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);
        mailSender.send(message);

        System.out.println(content);

    }

    public static void sendPasswordResetCode(String token, User user, JavaMailSender mailSender) throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "renitt3test@gmail.com";
        String senderName = "Rent It";
        String subject = "Password Reset";
        String content = "Dear [[name]],<br>"
                + "Password reset code:<br>"
                + token +"<br>"
                + "Thank you,<br>"
                + "Rent It.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getFirstName());

        helper.setText(content, true);
        mailSender.send(message);

        System.out.println(content);
    }
}