package com.page6.repository;

import com.page6.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);

    //특정 게시물의 태그를 리스트로 select
    @Query("SELECT t.name FROM Tag t JOIN TagMap tm On tm.tag.id = t.id WHERE tm.board.id = :bid")
    List<String> findTagNamesByBoardId(@Param("bid") Long bid);

    List<Tag> findByNameContaining(String tag);

    Tag findAllById(Long tagId);
}
