package com.page6.repository;

import com.page6.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    ////////////////////// 검색 //////////////////////
    //제목 포함 검색
    Page<Board> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    //본문 포함 검색
    Page<Board> findByContentContainingIgnoreCase(String content, Pageable pageable);

    //제목+본문 포함 검색
    Page<Board> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content, Pageable pageable);

    //작성자 포함 검색
    Page<Board> findByMemberNameContainingIgnoreCase(String keyword, Pageable pageable);

    //조회수 개수 플러스
    @Modifying
    @Query("update Board b set b.view_cnt = b.view_cnt + 1 where b.id = :id")
    int updateView(@Param("id") Long id);

    //좋아요 개수 플러스
    @Modifying
    @Query("update Board b set b.like_cnt = b.like_cnt + 1 where b.id = :id")
    int updateLike(@Param("id") Long id);

    // 페이징
    Page<Board> findAll(Pageable pageable);
}
