package com.page6.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HeartRequestDto {
//    private Long memberId;
    private Long boardId;

    public HeartRequestDto(Long boardId) {
        this.boardId = boardId;
    }
}
