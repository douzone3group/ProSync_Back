package com.douzone.prosync.bookmark.service;

import com.douzone.prosync.bookmark.dto.BookmarkResponseDto;
import com.douzone.prosync.bookmark.mapper.BookmarkMapper;
import com.douzone.prosync.bookmark.repository.BookmarkRepository;
import com.douzone.prosync.common.PageResponseDto;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService{

    private final BookmarkRepository bookmarkRepository;
    private final BookmarkMapper mapper;
    @Override
    public void bookMarkCheck(Long projectId, Long memberId) {
        bookmarkRepository.bookMarkCheck(projectId, memberId);
    }

    @Override
    public void bookMarkRemove(Long projectId, Long memberId) {
        bookmarkRepository.bookMarkRemove(projectId, memberId);
    }

    @Override
    public PageResponseDto<BookmarkResponseDto> findAll(Pageable pageable, Long memberId) {

        int pageNum = pageable.getPageNumber() == 0 ? 1 : pageable.getPageNumber();
        PageHelper.startPage(pageNum, pageable.getPageSize());

        List<BookmarkResponseDto> bookmarkList = mapper.findAll(memberId);
        PageInfo<BookmarkResponseDto> pageInfo = new PageInfo<>(bookmarkList);
        return new PageResponseDto<>(pageInfo);
    }
}