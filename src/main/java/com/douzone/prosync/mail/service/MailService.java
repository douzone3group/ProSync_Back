//package com.douzone.prosync.mail.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
//
//@Service
//@RequiredArgsConstructor
//public class MailService {
//
//    private final JavaMailSender javaMailSender;
//    private static final String senderEmail= "140326@naver.com";
//    private static int number;
//
//    public static void createNumber(){
//        number = (int)(Math.random() * (90000)) + 100000;// (int) Math.random() * (최댓값-최소값+1) + 최소값
//    }
//
//    public MimeMessage CreateMail(String mail){
//        createNumber();
//        MimeMessage message = javaMailSender.createMimeMessage();
//
//        try {
//            message.setFrom(senderEmail);
//            message.setRecipients(MimeMessage.RecipientType.TO, mail);
//            message.setSubject("이메일 인증");
//            String body = "";
//            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
//            body += "<h1>" + number + "</h1>";
//            message.setText(body,"UTF-8", "html");
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//
//        return message;
//    }
//
//    public int sendMail(String mail){
//        MimeMessage message = CreateMail(mail);
//        javaMailSender.send(message);
//
//        return number;
//    }
//}

package com.douzone.prosync.mail.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailService {

    private final JavaMailSender javaMailSender;
    private final String senderEmail;
    private static int number;

    public MailService(JavaMailSender javaMailSender, @Value("${mail.sender}") String senderEmail) {
        this.javaMailSender = javaMailSender;
        this.senderEmail = senderEmail;
    }

    public static void createNumber() {
        number = (int) (Math.random() * (90000)) + 100000;// (int) Math.random() * (최댓값-최소값+1) + 최소값
    }

    public MimeMessage CreateMail(String mail) {
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("이메일 인증");
            String body = "<html lang=\"ko\">"
                    + "<body style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; color: #333;\">"
                    + "<div style=\"background: #ffffff; padding: 30px; border-radius: 5px; max-width: 500px; margin: 0 auto; border: 5px solid #ff9d00; box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);\">"
                    + "<h1 style=\"font-size: 24px; color: #ff6c00; text-align: center; border-bottom: 2px solid #ff9d00;\">Prosync 인증 코드</h1>"
                    + "<p style=\"font-size: 16px; line-height: 1.5; color: #555; text-align: center; font-weight: bold\">아래 인증 코드를 입력하여 이메일 주소를 인증해 주세요.</p>"
                    + "<div style=\"background: #ff6c00; padding: 15px; border-radius: 5px; text-align: center; font-size: 32px; font-weight: bold; color: #ffffff; margin: 10px 0;\">" + number + "</div>"
                    + "<p style=\"font-size: 15px; color: #999; text-align: center; margin-top: 10px \">감사합니다.</p>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

    public String sendMail(String mail) {
        MimeMessage message = CreateMail(mail);
        javaMailSender.send(message);

        return Integer.toString(number);
    }
}