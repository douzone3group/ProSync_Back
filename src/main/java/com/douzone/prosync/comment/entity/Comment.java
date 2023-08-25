package com.douzone.prosync.comment.entity;


import com.douzone.prosync.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;
}
