package com.douzone.prosync.constant;

public class ConstantPool {

    // HEADER

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String REFRESH_HEADER = "Refresh";


    // EMAIL CERTIFICATIONNUMBER DURATION
    public static final long EMAIL_CERTIFICATIONNUMBER_DURATION = 600L;
    public static final long PROJECT_INVITE_LINK_DURATION = 1800L;

    // SSE TIMEOUT
    public static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;


    // page navigates(페이지 쪽수의 갯수)
    public static final int PAGE_NAVI = 5;

    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_PAGE_NUM = 1;


    // 프론트 서버 주소

    public static final String FRONT_SERVER_HOST = "http://localhost:3000";


    //  NOTIFICATION SCHEDULING DURATION
    public static final Integer NOTIFICATION_DEFAULT_DURATION = 30;


}