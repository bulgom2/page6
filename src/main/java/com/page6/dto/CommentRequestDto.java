package com.page6.dto;

import com.page6.entity.Board;
import com.page6.entity.Comment;
import com.page6.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequestDto {
    private Long id;
    private Board board;
    private Member member;
    private String content;
    private int depth;
    private LocalDateTime regdate = LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")));

    /* Dto -> Entity */
    public Comment toEntity() {
        Comment comments = Comment.builder()
                .id(id)
                .content(content)
                .board(board)
                .member(member)
                .content(content)
                .depth(depth)
                .regdate(regdate)
                .build();

        return comments;
    }
}