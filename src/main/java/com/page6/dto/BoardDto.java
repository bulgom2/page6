package com.page6.dto;

import com.page6.entity.Board;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
public class BoardDto {
    public Long id;            //게시글 인덱스

    public String writer;        // 작성자

    public String title;       // 제목

    public String content;       // 본문 내용

    public int like_cnt;     // 좋아요 개수

    public int view_cnt;     // 조회수

    public String regdate;    // 생성일자
    
    public Long comment_cnt;    //댓글 개수

//    public boolean delete_flag;      // 삭제여부

    public static BoardDto of(Board board) {
        return BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .writer(board.getMember().getName())
                .content(board.getContent())
                .like_cnt(board.getLike_cnt())
                .view_cnt(board.getView_cnt())
                .regdate(board.getRegdate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
