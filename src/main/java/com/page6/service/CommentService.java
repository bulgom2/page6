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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
//        commentEntity.setGroup(commentEntity.getId());

        commentRepository.save(commentEntity);
    }

    //bid 게시물의 댓글 리스트 얻기
    public List<CommentFormDto> getCommentList(Long bid) {
        Board board = boardRepository.findById(bid).get();
        List<Comment> list = commentRepository.findByBoard(board);
        List<Comment> result = new ArrayList<>();

        for(int i = 0; i < list.size(); i++) {
            Comment c = list.get(i);
            if(c.getDepth() == 0) {
                result.add(c);
                for(int j = i + 1; j < list.size(); j++) {
                    Comment child = list.get(j);
                    if(child.getDepth() == 1 && child.getGroup() == c.getId()) {
                        result.add(child);
                    }
                }
            }
        }


        return result
                .stream()
                .map(CommentFormDto::of)
                .collect(Collectors.toList());
    }

}
