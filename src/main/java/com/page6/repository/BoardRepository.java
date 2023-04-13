package com.page6.repository;

import com.page6.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Modifying
    @Query("update Board b set b.view_cnt = b.view_cnt + 1 where b.id = :id")
    int updateView(Long id);

}
