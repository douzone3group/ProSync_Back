package com.douzone.prosync.bookmark.controller;

import com.douzone.prosync.bookmark.dto.BookmarkResponseDto;
import com.douzone.prosync.bookmark.service.BookmarkService;
import com.douzone.prosync.common.PageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.douzone.prosync.constant.ConstantPool.DEFAULT_PAGE_SIZE;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService service;


    @PostMapping("/bookmark/{project-id}")
    void bookmark(@PathVariable("project-id") Long projectId, Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        service.bookMarkCheck(projectId, memberId);
    }

    @DeleteMapping("/bookmark/{project-id}")
    void bookmarkRemove(@PathVariable("project-id") Long projectId, Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        service.bookMarkRemove(projectId, memberId);
    }

    @GetMapping("/bookmark-list")
    PageResponseDto<BookmarkResponseDto> bookmarkList(@PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable, Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        PageResponseDto<BookmarkResponseDto> responseDto = service.findAll(pageable, memberId);
        return responseDto;
    }


}