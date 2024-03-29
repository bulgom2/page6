package com.page6.dto;

import com.page6.entity.Comment;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@Setter
@Getter
@AllArgsConstructor
public class CommentFormDto {
    public Long cid;         //댓글 인덱스
    public Long bid;        //작성 게시글 인덱스
    public String writer;   //작성자
    public String content;  //댓글 내용
    public int depth;       //깊이
    public Long group;       //댓글 그룹
    public String regdate; //등록일
//    public LocalDateTime regdate; //등록일

    public CommentFormDto() {}

    public CommentFormDto(Long cid, Long bid, String writer, String content, int depth, Long group) {
        this.cid = cid;
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
                .cid(comment.getId())
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
