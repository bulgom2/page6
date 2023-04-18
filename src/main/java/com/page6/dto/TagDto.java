package com.page6.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagDto {
    public String name;
    public Long boardId;
}
