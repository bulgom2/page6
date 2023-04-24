package com.page6.dto;

import com.page6.entity.Board;
import com.page6.entity.Deleted;
import com.page6.entity.Member;
import lombok.Builder;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
@Builder
public class DeletedDto {
    public Long id;        //삭제 인덱스
    public Board board;  //삭제 게시물 인덱스
    public Member member;  //삭제자 인덱스

    static ModelMapper modelMapper = new ModelMapper();

    public Deleted toEntity() {
        return modelMapper.map(this, Deleted.class);
    }
}