package com.page6.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardFormDto {

    public long no;            //게시글 인덱스

    public long writer;        // 작성자

    public String title;       // 제목

    public long content;       // 본문 내용

    public int like_count;     // 좋아요 개수

    public int view_count;     // 조회수

    public String regdt;    // 생성일자

    public String moddt;    // 수정일자

    public boolean delfl;      // 삭제여부
}
