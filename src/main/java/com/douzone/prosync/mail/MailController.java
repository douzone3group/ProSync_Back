package com.douzone.prosync.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @ResponseBody
    @PostMapping("/mail")
    public String MailSend(@RequestBody MailDto mail){

        System.out.println(mail.getMail());
        int number = mailService.sendMail(mail.getMail());

        String num = "" + number;

        return num;
    }

}
