package com.page6.dto;

import com.page6.entity.Board;
import lombok.Builder;
import lombok.Data;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
@Builder
public class BoardDto {
    public Long id;            //게시글 인덱스

    public String writer;        // 작성자

    public String title;       // 제목

    public String content;       // 본문 내용

    public int likes;     // 좋아요 개수

    public int views;     // 조회수

    public String regdate;    // 생성일자

    public String dateBefore;   // 날짜 차이
    
    public Long comment_cnt;    //댓글 개수

//    public boolean delete_flag;      // 삭제여부

    public static BoardDto of(Board board) {

        return BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .writer(board.getMember().getName())
                .content(board.getContent())
                .likes(board.getLikes())
                .views(board.getViews())
                .regdate(board.getRegdate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .dateBefore(dateBefore(board.getRegdate()))
                .build();
    }

    //localdatetime을 원하는 형식으로 반환해주는 메소드
    public static String dateBefore(LocalDateTime regdate) {
        String displayRegdate = "";
        ZoneId zoneId = ZoneId.of("Asia/Seoul");

        LocalDateTime now = LocalDateTime.now(Clock.system(zoneId)); //현재시간
        Duration duration = Duration.between(regdate, now);
        long diffHours = duration.toHours();
        long diffMinutes = Math.abs(duration.toMinutes());

        if (diffHours < 24) {
            if (diffHours < 1) {
                displayRegdate = diffMinutes + "분 전";
            }
            else {
                displayRegdate = diffHours + "시간 전";
            }
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            displayRegdate = regdate.format(formatter);
        }

        if (diffMinutes == 0) {
            displayRegdate = "방금";
        }


        return displayRegdate;
    }

}
