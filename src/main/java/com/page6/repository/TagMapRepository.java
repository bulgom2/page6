package com.page6.repository;

import com.page6.entity.Tag;
import com.page6.entity.TagMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagMapRepository extends JpaRepository<TagMap, Long> {
    List<TagMap> findAllByTag(Tag tag);

    List<TagMap> findAllByBoardId(Long boardId);

    //특정 게시물에 특정 태그가 있는지 확인하는 메소드
    Optional<TagMap> findByBoardIdAndTagName(Long boardId, String tagName);

    //특정 id tagmap 삭제
    void deleteById(Long id);
}
