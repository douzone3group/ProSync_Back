package com.douzone.prosync.bookmark.mapper;

import com.douzone.prosync.bookmark.dto.BookmarkResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookmarkMapper {
    void bookMarkCheck(@Param("projectId") Long projectId, @Param("memberId") Long memberId);
    void bookMarkRemove(@Param("projectId") Long projectId, @Param("memberId") Long memberId);

    List<BookmarkResponseDto> findAll(Long memberId);
}
