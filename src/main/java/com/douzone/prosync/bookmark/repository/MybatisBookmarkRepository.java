package com.douzone.prosync.bookmark.repository;

import com.douzone.prosync.bookmark.mapper.BookmarkMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MybatisBookmarkRepository implements BookmarkRepository {

    private final BookmarkMapper bookmarkMapper;

    @Override
    public void bookMarkCheck(Long projectId, Long memberId) {
        bookmarkMapper.bookMarkCheck(projectId, memberId);
    }

    @Override
    public void bookMarkRemove(Long project_id, Long member_id) {
        bookmarkMapper.bookMarkRemove(project_id, member_id);
    }
}