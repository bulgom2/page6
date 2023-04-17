package com.page6.service;

import com.page6.dto.BoardDto;
import com.page6.entity.Board;
import com.page6.repository.BoardRepository;
import com.page6.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    private final MemberRepository memberRepository;
    private final CommentService commentService;


    //글쓰기 저장 기능
    public void write(Board board, String email){
        board.setMember(memberRepository.findByEmail(email));
        boardRepository.save(board);
    }

    //리스트 조회 기능
//    public List<Board> getBoardList() {
//        return boardRepository.findAll();
//    }
//    public List<BoardDto> getBoardList() {
//        List<Board> list = boardRepository.findAll();
//        return list
//                .stream()
//                .map(BoardDto::of)
//                .collect(Collectors.toList());
//    }

    public List<BoardDto> getBoardList() {
        List<Board> list = boardRepository.findAll();
        return list
                .stream()
                .map(board -> {
                    BoardDto dto = BoardDto.of(board);
                    dto.setComment_cnt(commentService.getCommentCount(board.getId()));
                    return dto;
                        })
                .collect(Collectors.toList());
    }

    //게시글 조회 시 하나 선택하는 기능
//    public Optional<Board> BoardOne(long id) {
//        return boardRepository.findById(id);
//    }

    //게시글 조회 시 하나 선택하는 기능
    public BoardDto BoardOne(long id) {
        Optional<Board> board = boardRepository.findById(id);
        return BoardDto.of(board.get());
    }

    //게시물 조회수 증가
    @Transactional
    public void viewCntUpdate(Long id) {
        boardRepository.updateView(id);
    }

    // 동진 페이징
   // 페이징 리스트 조회 기능
    public Page<BoardDto> findAll(Pageable pageable) {
        Page<Board> boardPage = boardRepository.findAll(pageable);
        List<BoardDto> boardDtoList = boardPage.getContent()
                .stream()
                .map(board -> {
                    BoardDto boardDto = BoardDto.of(board);
                    boardDto.setComment_cnt(commentService.getCommentCount(board.getId()));
                    return boardDto;
                })
                .collect(Collectors.toList());
        return new PageImpl<>(boardDtoList, pageable, boardPage.getTotalElements());
    }

//    // 내 페이징
//    public Page<Board> boardList(Pageable pageable){
//        //기존 List<Board>값으로 넘어가지만 페이징 설정을 해주면 Page<Board>로 넘어갑니다.
//        return boardRepository.findAll(pageable);
//    }

}