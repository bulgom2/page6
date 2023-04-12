package com.page6.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardDto {
    public long id;            //게시글 인덱스

//    public long writer;        // 작성자

    public String title;       // 제목

    public long content;       // 본문 내용

    public int like_cnt;     // 좋아요 개수

    public int view_cnt;     // 조회수

    public LocalDateTime regdate;    // 생성일자

    public boolean delete_flag;      // 삭제여부

}
