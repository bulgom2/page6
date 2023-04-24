package com.page6.dto;

import com.page6.entity.Board;
import com.page6.entity.DeleteLog;
import com.page6.entity.Member;
import lombok.Builder;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.time.format.DateTimeFormatter;

@Data
@Builder
public class DeleteLogDto {
    public Long id;         //인덱스
    public String title;     //삭제 게시물 제목
    public String member;   //삭제자 or 복구자 인덱스
    public String flag;     //삭제 이력인지 복구 이력인지
    public String regdate;  //날짜

    static ModelMapper modelMapper = new ModelMapper();

//    public DeleteLog toEntity() {
//        return modelMapper.map(this, DeleteLog.class);
//    }

    public static DeleteLogDto of(DeleteLog log) {

        return DeleteLogDto.builder()
                .id(log.getId())
                .title(log.getBoard().getTitle())
                .flag(log.isFlag() ? "복구" : "삭제")
                .regdate(log.getRegdate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
