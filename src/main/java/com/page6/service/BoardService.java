package com.page6.service;

import com.page6.dto.BoardDto;
import com.page6.entity.Board;
import com.page6.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    //저장 기능
    public void write(Board board){
        boardRepository.save(board);
    }

    //리스트 조회 기능
//    public List<Board> getBoardList() {
//        return boardRepository.findAll();
//    }
    public List<BoardDto> getBoardList() {
        List<Board> list = boardRepository.findAll();
        return list
                .stream()
                .map(BoardDto::of)
                .collect(Collectors.toList());
    }

    //게시글 조회 시 하나 선택하는 기능
//    public Optional<Board> BoardOne(long id) {
//        return boardRepository.findById(id);
//    }

    public Optional<Board> BoardOne(long id) {
        Optional<Board> board = boardRepository.findById(id);

        return boardRepository.findById(id);
    }

    //게시물 조회수 증가
    @Transactional
    public void viewCntUpdate(long id) {
        boardRepository.updateView(id);
    }

    // 페이징

}