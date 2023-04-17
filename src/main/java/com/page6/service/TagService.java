package com.page6.service;

import com.page6.entity.Board;
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
        TagMap map = new TagMap();
        Board board = boardRepository.findById(bid).get();
        boolean flag = tagRepository.findByName(tagName).orElse(null) == null;
//        boolean flag = tagRepository.findByName(tagName).isPresent();


        //같은 Tag가 없을 경우 tag 생성
        if(flag) {
            Tag tag = new Tag();
            tag.setName(tagName);
            tagRepository.save(tag);
        }

        //tag-board map 생성
        Tag tag = tagRepository.findByName(tagName).get();
        map.setTag(tag);
        map.setBoard(board);
        tagMapRepository.save(map);
    }
}
