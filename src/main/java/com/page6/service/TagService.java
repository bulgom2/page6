package com.page6.service;

import com.page6.dto.TagDto;
import com.page6.entity.Board;
import com.page6.entity.Tag;
import com.page6.entity.TagMap;
import com.page6.repository.BoardRepository;
import com.page6.repository.TagMapRepository;
import com.page6.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final TagMapRepository tagMapRepository;
    private final BoardRepository boardRepository;

    //태그 추가
    public void addTag(Long bid, String tagName) {
        Board board = boardRepository.findById(bid)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        //Tag가 이미 존재하는지 확인하고 없으면 생성
        Tag tag = tagRepository.findByName(tagName).orElseGet(() -> {
            Tag newTag = new Tag();
            newTag.setName(tagName);
            return tagRepository.save(newTag);
        });

        // tag-board map 생성
        TagMap map = new TagMap();
        map.setTag(tag);
        map.setBoard(board);
        tagMapRepository.save(map);
    }

    //태그 출력
    public List<String> getTagList(Long bid) {
        return tagRepository.findTagNamesByBoardId(bid);
    }
}

