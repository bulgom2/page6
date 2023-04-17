package com.page6.service;

import com.page6.entity.Board;
import com.page6.entity.Heart;
import com.page6.entity.Member;
import com.page6.repository.BoardRepository;
import com.page6.repository.HeartRepository;
import com.page6.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HeartService {
    private final HeartRepository heartRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    //좋아요했는지 조회하는 기능
    public boolean heartFlag(Long bid, String email) {
        Board board = boardRepository.findById(bid).get();
        Member member = memberRepository.findByEmail(email);
        boolean flag = heartRepository.findByBoardAndMember(board, member).orElse(null) != null;
        return flag;   //존재하면 true, 존재하지 않으면 false
    }

    //좋아요 추가 기능
    @Transactional
    public void addHeart(Long bid, String email) {
        Board board = boardRepository.findById(bid).get();
        Member member = memberRepository.findByEmail(email);
        Heart heart = new Heart(board, member);
        heartRepository.save(heart);
        boardRepository.updateLike(bid);
    }
}
