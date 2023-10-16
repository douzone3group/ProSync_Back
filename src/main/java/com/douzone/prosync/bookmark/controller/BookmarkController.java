package com.douzone.prosync.bookmark.controller;

import com.douzone.prosync.bookmark.dto.BookmarkDto;
import com.douzone.prosync.bookmark.dto.BookmarkResponseDto;
import com.douzone.prosync.bookmark.entity.Bookmark;
import com.douzone.prosync.bookmark.service.BookmarkService;
import com.douzone.prosync.common.PageResponseDto;
import com.douzone.prosync.project.dto.response.GetProjectResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;

import static com.douzone.prosync.constant.ConstantPool.DEFAULT_PAGE_SIZE;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name="bookmark", description = "북마크 API")
public class BookmarkController {

    private final BookmarkService service;

    @GetMapping("/bookmark/{project-id}")
    @ApiOperation(value ="북마크 단일 조회", notes = "북마크 단일 조회", tags = "bookmark")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = BookmarkResponseDto.class),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 500, message = "Server Error")
    })
    ResponseEntity getBookmark(@Parameter(description = "프로젝트 식별자", required = true, example = "1")
                               @PathVariable("project-id") Long projectId,
                               @Parameter(hidden = true) @ApiIgnore Principal principal) {
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