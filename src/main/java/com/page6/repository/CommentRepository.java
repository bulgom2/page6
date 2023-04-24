package com.page6.repository;

import com.page6.entity.Board;
import com.page6.entity.Comment;
import com.page6.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    //특정 게시물의 댓글 리스트
    List<Comment> findByBoard(Board board);

    //특정 게시물의 댓글 개수 카운트
    Long countCommentByBoard(Board board);

    // 특정 유저의 댓글 리스트
    Page<Comment> findAllByWriter(Member member, Pageable pageable);

    //

}
