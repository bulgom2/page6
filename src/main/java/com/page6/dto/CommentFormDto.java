package com.page6.dto;

import com.page6.entity.Comment;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentFormDto {
    public long bid;        //작성 게시글 인덱스
    public String writer;   //작성자
    public String content;  //댓글 내용
    public int depth;       //깊이
    public long group;       //댓글 그룹

    public CommentFormDto() {}

    public CommentFormDto(long bid, String writer, String content, int depth, long group) {
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
                .bid(comment.getId())
                .writer(comment.getWriter().getName())
                .content(comment.getContent())
                .depth(comment.getDepth())
                .group(comment.getGroup())
                .build();
    }
}
