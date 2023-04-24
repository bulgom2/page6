package com.page6.service;

import com.page6.entity.Board;
import com.page6.entity.DeleteLog;
import com.page6.entity.Member;
import com.page6.repository.BoardRepository;
import com.page6.repository.DeleteLogRepository;
import com.page6.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DeleteLogService {
    private final DeleteLogRepository deleteLogRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public void updateDeletedLog(Long bid, String email, boolean flag) {
        Board board = boardRepository.findById(bid).get();
        Member member = memberRepository.findByEmail(email);
        DeleteLog log = new DeleteLog(board, member, flag);
        deleteLogRepository.save(log);
    }
}
