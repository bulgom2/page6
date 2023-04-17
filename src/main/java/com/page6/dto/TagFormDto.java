package com.page6.dto;

import com.page6.entity.Comment;
import com.page6.entity.Tag;
import com.page6.entity.TagMap;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagFormDto {
    public String name;
    public Long boardId;

}
