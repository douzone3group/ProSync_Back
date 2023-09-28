package com.douzone.prosync.bookmark.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookmarkDto {

    private Long bookmark_id;
    private Long project_id;
    private Long member_id;

    public static BookmarkDto of(BookmarkDto bookmark) {
        return BookmarkDto.builder()
                .bookmark_id(bookmark.getBookmark_id())
                .project_id(bookmark.getProject_id())
                .member_id(bookmark.getMember_id())
                .build();
    }

}
