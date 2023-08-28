package com.douzone.prosync.notification.notienum;

public enum NotificationCode {
    TASK_REMOVE("업무삭제"), TASK_ASSIGNMENT("업무지정"), TASK_MODIFICATION("업무수정"), TASK_EXCLUDED("업무제외"),
    PROJECT_ASSIGNMENT("프로젝트지정"), PROJECT_EXCLUDED("프로젝트제외"), PROJECT_AUTHORITY_CHANGE("프로젝트권한변경"),
    PROJECT_MODIFICATION("프로젝트수정"), PROJECT_REMOVE("프로젝트삭제"),
    COMMENT_ADD("댓글추가"),  COMMENT_REMOVE("댓글삭제"),  COMMENT_MODIFICATION("댓글수정"), CHANGE_ADMIN("관리자변경");


    final private String code;

    NotificationCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
