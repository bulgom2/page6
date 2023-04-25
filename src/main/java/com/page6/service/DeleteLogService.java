package com.page6.service;

import com.page6.dto.BoardDto;
import com.page6.dto.DeleteLogDto;
import com.page6.entity.Board;
import com.page6.entity.DeleteLog;
import com.page6.entity.Member;
import com.page6.repository.BoardRepository;
import com.page6.repository.DeleteLogRepository;
import com.page6.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    //삭제 복구 이력 확인하기
    public Page<DeleteLogDto> getDeletedBoard(Pageable pageable) {
        Page<DeleteLog> myList = deleteLogRepository.findAll(pageable);

        List<DeleteLogDto> dtoList = myList.stream()
                .map(DeleteLogDto::of)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, myList.getTotalElements());
    }

}
