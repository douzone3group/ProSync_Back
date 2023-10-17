package com.douzone.prosync.bookmark.service;

import com.douzone.prosync.bookmark.dto.BookmarkDto;
import com.douzone.prosync.bookmark.dto.BookmarkResponseDto;
import com.douzone.prosync.bookmark.mapper.BookmarkMapper;
import com.douzone.prosync.bookmark.repository.BookmarkRepository;
import com.douzone.prosync.common.PageResponseDto;
import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService{

    private final BookmarkRepository bookmarkRepository;
    private final BookmarkMapper mapper;
    @Override
    public void bookMarkCheck(Long projectId, Long memberId) {
        int duplicate = mapper.duplicateBookmark(projectId, memberId);
        if (duplicate == 0) {
            mapper.bookMarkCheck(projectId, memberId);
        } else if (duplicate >0 ){
            mapper.bookMarkRemove(projectId, memberId);
        }
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

    @Override
    public BookmarkDto findBookmark(Long projectId, Long memberId) {
        Optional<BookmarkDto> bookmark = mapper.findById(projectId, memberId);
        System.out.println("service bookmark = " + bookmark);
        return bookmark.orElseThrow(() -> new ApplicationException(ErrorCode.PROJECT_NOT_FOUND));

    }
}