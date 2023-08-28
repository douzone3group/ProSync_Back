package com.douzone.prosync.log.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
// Todo: Log 로직 작성하기
public class LogController {

    public String method() {
        return "실행되라";

    }
}
