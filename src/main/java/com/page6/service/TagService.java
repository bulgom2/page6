package com.page6.service;

import com.page6.entity.Tag;
import com.page6.entity.TagMap;
import com.page6.repository.BoardRepository;
import com.page6.repository.TagMapRepository;
import com.page6.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final TagMapRepository tagMapRepository;
    private final BoardRepository boardRepository;

    //태그 추가
    public void addTag(Long bid, String tagName) {
        Tag tag = new Tag();
        TagMap map = new TagMap();
        tag.setName(tagName);
        
    }
}
