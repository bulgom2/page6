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

   //키워드 검색
    public Page<BoardDto> findAll(String keyword, Pageable pageable) {
        Page<Board> boardPage = keyword == null || keyword.isEmpty() || "".equals(keyword) ?
                boardRepository.findAll(pageable) :
                boardRepository.findByTitleContainingIgnoreCase(keyword, pageable);
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

    // 이름 정렬 페이징
//    public Page<BoardDto> findAllByOrderByNameAsc(Pageable pageable) {
//        Page<Board> boardPage = boardRepository.findAllByOrderByNameAsc(pageable);
//        List<BoardDto> boardDtoList = boardPage.getContent()
//                .stream()
//                .map(board -> {
//                    BoardDto boardDto = BoardDto.of(board);
//                    boardDto.setComment_cnt(commentService.getCommentCount(board.getId())); // 댓글 개수
//                    return boardDto;
//                })
//                .collect(Collectors.toList());
//        return new PageImpl<>(boardDtoList, pageable, boardPage.getTotalElements());
//    }
//    public List<Board> findAllByOrderByName() {
//        return boardRepository.findAllByOrderByName();
//    }

    //BoardDto 이름 찾기
    public Page<BoardDto> findAllByName(Pageable pageable) {
        Page<Board> boardPage = boardRepository.findAllByOrderByName(pageable);
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
    public Page<Board> findAllByOrderByName(Pageable pageable) {

        return boardRepository.findAllByOrderByName(pageable);
    }
    public Page<Board> findAllByOrderByTitle(Pageable pageable) {
        return boardRepository.findAllByOrderByTitle(pageable);
    }

    public Page<Board> findAllByOrderByIdDesc(Pageable pageable) {
        return boardRepository.findAllByOrderByIdDesc(pageable);
    }

    public Page<Board> findAllByOrderByLikesDesc(Pageable pageable) {
        return boardRepository.findAllByOrderByLikesDesc(pageable);
    }

    public Page<Board> findAllByOrderByViewsDesc(Pageable pageable) {
        return boardRepository.findAllByOrderByViewsDesc(pageable);
    }



//    // 내 페이징
//    public Page<Board> boardList(Pageable pageable){
//        //기존 List<Board>값으로 넘어가지만 페이징 설정을 해주면 Page<Board>로 넘어갑니다.
//        return boardRepository.findAll(pageable);
//    }


    ////////////////////////    서치    ////////////////////////
    //제목 포함 검색
    @Transactional
    public Page<Board> searchByTitleContaing(String title, Pageable pageable) {
        return boardRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    //본문 포함 검색
    @Transactional
    public Page<Board> searchByContentContaing(String content, Pageable pageable) {
        return boardRepository.findByContentContainingIgnoreCase(content, pageable);
    }

    //제목+본문 포함 검색
    @Transactional
    public Page<Board> searchByTitleContentContaing(String keyword, Pageable pageable) {
        return boardRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageable);
    }

    //작성자 포함 검색
    @Transactional
    public Page<Board> searchByWriterContaing(String keyword, Pageable pageable) {
        return boardRepository.findByMemberNameContainingIgnoreCase(keyword, pageable);
    }

    //해시태그 포함 검색
    @Transactional
    public List<Board> searchByTagContaing(String keyword) {
        return boardRepository.findByTagContaing(keyword);
    }

    //해시태그 검색
    @Transactional
    public List<Board> searchByTag(String keyword) {
        return boardRepository.findByTagName(keyword);
    }

}