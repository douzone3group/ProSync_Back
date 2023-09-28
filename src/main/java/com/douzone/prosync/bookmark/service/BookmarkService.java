package com.douzone.prosync.bookmark.service;

import com.douzone.prosync.bookmark.dto.BookmarkDto;
import com.douzone.prosync.bookmark.dto.BookmarkResponseDto;
import com.douzone.prosync.common.PageResponseDto;
import org.springframework.data.domain.Pageable;

public interface BookmarkService {
    void bookMarkCheck(Long projectId, Long memberId);

    void bookMarkRemove(Long projectId, Long memberId);

    PageResponseDto<BookmarkResponseDto> findAll(Pageable pageable, Long memberId);

    BookmarkDto findBookmark(Long projectId, Long memberId);
}
