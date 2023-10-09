package com.douzone.prosync.constant;

public class ConstantPool {

    // HEADER

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String REFRESH_HEADER = "Refresh";


    // EMAIL CERTIFICATIONNUMBER DURATION
    public static final long EMAIL_CERTIFICATIONNUMBER_DURATION = 600L;
    public static final long PROJECT_INVITE_LINK_DURATION = 1800L;

    // SSE TIMEOUT
    public static final Long DEFAULT_TIMEOUT = 60L * 1000 * 30;


    // page navigates(페이지 쪽수의 갯수)
    public static final int PAGE_NAVI = 5;

    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_PAGE_NUM = 1;


    // 프론트 서버 주소

    public static final String FRONT_SERVER_HOST = "http://prosyncfront.s3-website.ap-northeast-2.amazonaws.com";


    //  NOTIFICATION SCHEDULING DURATION

    public static final Integer NOTIFICATION_EXPIRATION_DURATION = 30*6;




    // LOG SCHEDULING DURATION
    public static final Long LOG_SCHEDULING_TERM = 1L* 24 * 60 * 60 *1000;

    public static final Integer LOG_EXPIRATION_DURATION = 365 * 3;


}