package com.douzone.prosync.notification.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ContentUrlContainer {
    public String content;
    public String url;
    public LocalDateTime date;
}
