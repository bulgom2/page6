package com.page6.dto;

import com.page6.entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
public class BoardFormDto {

    public Long id;            //게시글 인덱스

    public String email;        // 작성자 이메일

    public String title;       // 제목

    public String content;       // 본문 내용


    public static BoardFormDto of(Board board) {

        return BoardFormDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .email(board.getMember().getEmail())
                .content(board.getContent())
                .build();
    }

    public Board toEntity() {
        Board board = new Board();
        board.setId(this.getId());
        board.setTitle(this.getTitle());
        board.setContent(this.getContent());
        return board;
    }
}
