package com.page6.repository;

import com.page6.dto.BoardDto;
import com.page6.entity.Board;
import com.page6.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Dictionary;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 특정 멤버가 작성한 글 모두 찾기
    Page<Board> findAllByMemberAndDeletedFalse(Member member, Pageable pageable);

    ////////////////////// 검색 //////////////////////
    //제목 포함 검색
    Page<Board> findByTitleContainingIgnoreCaseAndDeletedFalse(String title, Pageable pageable);

    //본문 포함 검색
    Page<Board> findByContentContainingIgnoreCaseAndDeletedFalse(String content, Pageable pageable);

    //제목+본문 포함 검색
    Page<Board> findByTitleContainingIgnoreCaseAndDeletedFalseOrContentContainingIgnoreCaseAndDeletedFalse(String title, String content, Pageable pageable);

    //작성자 포함 검색
    Page<Board> findByMemberNameContainingIgnoreCaseAndDeletedFalse(String keyword, Pageable pageable);

    //해시태그 검색
    @Query("SELECT b FROM Board b JOIN TagMap tm ON b.id = tm.board.id JOIN Tag t ON tm.tag.id = t.id WHERE t.name = :name")
    Page<Board> findByTagName(@Param("name") String name, Pageable pageable);

    //해시태그 포함 검색
    @Query("SELECT DISTINCT b FROM Board b JOIN TagMap tm ON b.id = tm.board.id JOIN Tag t ON tm.tag.id = t.id WHERE t.name LIKE %:name%")
    Page<Board> findByTagContaing(@Param("name") String name, Pageable pageable);

    //다중 해시태그 포함 검색
//    Page<Board> findByTagMapTagNameIn(String[] tags, Pageable pageable);
    @Query("SELECT DISTINCT b FROM Board b JOIN TagMap tm ON b.id = tm.board.id JOIN Tag t ON tm.tag.id = t.id WHERE t.name IN :tags")
    List<Board> findByTagsContaining(@Param("tags") List<String> tags);
//    Page<Board> findByTagNamesContaining(String[] tags, Pageable pageable);


    //조회수 개수 플러스
    @Modifying
    @Query("update Board b set b.views = b.views + 1 where b.id = :id")
    int updateView(@Param("id") Long id);

    //좋아요 개수 플러스
    @Modifying
    @Query("update Board b set b.likes = b.likes + 1 where b.id = :id")
    int updateLike(@Param("id") Long id);

    // 페이징
    Page<Board> findAllByDeletedFalse(Pageable pageable);

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


    // 삭제 & 복구 기능
    @Modifying
    @Query("update Board b set b.deleted = :deleted where b.id = :id")
    int updateDeleted(@Param("id") Long id, @Param("deleted") boolean deleted);

    // 삭제 되지 않은 글 하나 조회하기
    Optional<Board> findByIdAndDeletedFalse(Long id);

    // 삭제된 글 모두 조회
    Page<Board> findAllByDeletedTrue(Pageable pageable);

}
