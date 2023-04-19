package com.page6.repository;

import com.page6.entity.Tag;
import com.page6.entity.TagMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagMapRepository extends JpaRepository<TagMap, Long> {
    List<TagMap> findAllByTag(Tag tag);
}
