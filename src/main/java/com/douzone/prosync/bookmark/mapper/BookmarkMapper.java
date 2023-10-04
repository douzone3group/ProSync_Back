package com.douzone.prosync.bookmark.mapper;


import com.douzone.prosync.bookmark.dto.BookmarkDto;
import com.douzone.prosync.bookmark.dto.BookmarkResponseDto;
import com.douzone.prosync.bookmark.entity.Bookmark;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BookmarkMapper {
    void bookMarkCheck(@Param("projectId") Long projectId, @Param("memberId") Long memberId);
    void bookMarkRemove(@Param("projectId") Long projectId, @Param("memberId") Long memberId);

    Optional<BookmarkDto> findById(@Param("projectId") Long projectId, @Param("memberId") Long memberId);
    List<BookmarkResponseDto> findAll(Long memberId);

    int duplicateBookmark(@Param("projectId") Long projectId, @Param("memberId") Long memberId);
}
