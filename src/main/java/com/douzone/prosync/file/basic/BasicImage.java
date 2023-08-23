package com.douzone.prosync.file.basic;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BasicImage {

    BASIC_USER_IMAGE("https://prosync-image.s3.ap-northeast-2.amazonaws.com/basic_user_image.png"),
    BASIC_PROJECT_IMAGE("https://prosync-image.s3.ap-northeast-2.amazonaws.com/basic_project_image.jpg"),;

    private String path;
}
