package com.page6.dto;

import com.page6.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@Setter
@AllArgsConstructor
public class CommentFormDto {
    public Long bid;        //작성 게시글 인덱스
    public String writer;   //작성자
    public String content;  //댓글 내용
    public int depth;       //깊이
    public Long group;       //댓글 그룹
    public String regdate; //등록일
//    public LocalDateTime regdate; //등록일

    public CommentFormDto() {}

    public CommentFormDto(Long bid, String writer, String content, int depth, Long group) {
        this.bid = bid;
        this.writer = writer;
        this.content = content;
        this.depth = depth;
        this.group = group;
    }

    public Comment toEntity() {
        return Comment.builder()
                .content(content)
                .depth(depth)
                .group(group)
                .build();
    }

    public static CommentFormDto of(Comment comment) {
        return CommentFormDto.builder()
                .bid(comment.getBoard().getId())
                .writer(comment.getWriter().getName())
                .content(comment.getContent())
                .depth(comment.getDepth())
                .group(comment.getGroup())
//                .regdate(comment.getRegdate())
                .regdate(comment.getRegdate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
