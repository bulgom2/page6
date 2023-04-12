package com.page6.service;

import com.page6.entity.Board;
import com.page6.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    //저장 기능
    public void write(Board board){
        boardRepository.save(board);
    }

    //리스트 조회 기능
    public List<Board> getBoardList() {
        return boardRepository.findAll();
    }

    //게시글 조회 시 하나 선택하는 기능
    public Optional<Board> BoardOne(long id) {
        return boardRepository.findById(id);
    }
}