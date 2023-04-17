package com.page6.repository;

import com.page6.dto.BoardDto;
import com.page6.entity.Board;
import com.page6.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

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

//    // 제목 오름차순 정렬
//    Page<Board> findAllByOrderByTitleAsc(Pageable pageable);
//
//    // 닉네임 오른차순 정렬
//    Page<Board> findAllByOrderByMemberNameAsc();
//
//    // 등록일 오름차순 정렬
//    Page<Board> findAllByOrderByRegdateAsc();

}
