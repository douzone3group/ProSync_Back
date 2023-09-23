package com.douzone.prosync.bookmark.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BookmarkResponseDto {
    private Long bookmarkId;
    private Long projectId;
    private String title;
    private LocalDateTime created_at;
}
