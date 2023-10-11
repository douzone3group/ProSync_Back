package com.douzone.prosync.bookmark.dto;

import com.douzone.prosync.bookmark.entity.Bookmark;
import com.douzone.prosync.project.dto.response.GetProjectResponse;
import com.douzone.prosync.project.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookmarkResponseDto {
    private Long bookmarkId;
    private Long projectId;
    private String title;
    private LocalDateTime created_at;
    private String projectImage;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
}
