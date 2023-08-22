package com.douzone.prosync.comment.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;


    private String content;

    private Long taskId;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private Boolean isDeleted;

}
