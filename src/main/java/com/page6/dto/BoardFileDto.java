package com.page6.dto;

import com.page6.entity.BoardFile;
import org.modelmapper.ModelMapper;

public class BoardFileDto {
    private Long id;        //파일 인덱스

    private String fileName; //원본 이미지 파일명

    private String oriFileName; //원본 파일명

    private String filePath;    //파일 조회 경로(책에서 imgUrl)

    private static ModelMapper modelMapper = new ModelMapper();

    public static BoardFileDto of(BoardFile boardFile) {
        return modelMapper.map(boardFile, BoardFileDto.class);
    }
}
