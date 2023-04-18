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

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    //조회수 개수 플러스
    @Modifying
    @Query("update Board b set b.views = b.views + 1 where b.id = :id")
    int updateView(@Param("id") Long id);

    //좋아요 개수 플러스
    @Modifying
    @Query("update Board b set b.likes = b.likes + 1 where b.id = :id")
    int updateLike(@Param("id") Long id);

    // 페이징
    Page<Board> findAll(Pageable pageable);


    // 이름 오름차순 정렬
    @Query("SELECT b from Board b join b.member m order by m.name asc, b.title asc")
    Page<Board> findAllByOrderByName(Pageable pageable);


    // 제목 오름차순 정렬
    Page<Board> findAllByOrderByTitle(Pageable pageable);

    // 인덱스 내림차순 정렬
    Page<Board> findAllByOrderByIdDesc(Pageable pageable);


    // 좋아요 내림차순 정렬
    Page<Board> findAllByOrderByLikesDesc(Pageable pageable);

    // 조회수 내림차순 정렬
    Page<Board> findAllByOrderByViewsDesc(Pageable pageable);

//

}
