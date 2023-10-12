package com.douzone.prosync.bookmark.controller;

import com.douzone.prosync.bookmark.dto.BookmarkDto;
import com.douzone.prosync.bookmark.dto.BookmarkResponseDto;
import com.douzone.prosync.bookmark.entity.Bookmark;
import com.douzone.prosync.bookmark.service.BookmarkService;
import com.douzone.prosync.common.PageResponseDto;
import com.douzone.prosync.project.dto.response.GetProjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.douzone.prosync.constant.ConstantPool.DEFAULT_PAGE_SIZE;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService service;

    @GetMapping("/bookmark/{project-id}")
    ResponseEntity getBookmark(@PathVariable("project-id") Long projectId, Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        BookmarkDto bookmark=service.findBookmark(projectId, memberId);
        System.out.println("controller bookmark = " + bookmark);

        return new ResponseEntity(BookmarkDto.of(bookmark), HttpStatus.OK);
    }


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
    PageResponseDto<BookmarkResponseDto> bookmarkList(@PageableDefault(size = 8) Pageable pageable, Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        PageResponseDto<BookmarkResponseDto> responseDto = service.findAll(pageable, memberId);
        return responseDto;
    }


}