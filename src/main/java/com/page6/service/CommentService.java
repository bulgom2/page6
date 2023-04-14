package com.page6.service;

import com.page6.dto.CommentFormDto;
import com.page6.entity.Board;
import com.page6.entity.Comment;
import com.page6.entity.Member;
import com.page6.repository.BoardRepository;
import com.page6.repository.CommentRepository;
import com.page6.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    //댓글 등록
    public void commentAdd(CommentFormDto dto) {

        Member member = memberRepository.findByEmail(dto.getWriter());
        Board board = boardRepository.findById(dto.getBid()).get();

        Comment commentEntity = dto.toEntity();

        commentEntity.setWriter(member);
        commentEntity.setBoard(board);

        commentRepository.save(commentEntity);
    }
}
