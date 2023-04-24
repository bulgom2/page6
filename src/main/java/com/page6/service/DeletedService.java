package com.page6.service;

import com.page6.entity.Board;
import com.page6.entity.Deleted;
import com.page6.entity.Member;
import com.page6.repository.BoardRepository;
import com.page6.repository.DeletedRepository;
import com.page6.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DeletedService {
    private final DeletedRepository deletedRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public void updateDeletedLog(Long bid, String email, boolean flag) {
        Board board = boardRepository.findById(bid).get();
        Member member = memberRepository.findByEmail(email);
        Deleted deleted = new Deleted(board, member, flag);
        deletedRepository.save(deleted);
    }
}
