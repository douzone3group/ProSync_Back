package com.douzone.prosync.bookmark.repository;



public interface BookmarkRepository {
    void bookMarkCheck(Long project_id, Long member_id);
    void bookMarkRemove(Long project_id, Long member_id);

}
