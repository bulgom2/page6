package com.page6;

import com.page6.controller.BoardController;
import com.page6.dto.BoardDto;
import com.page6.dto.CommentFormDto;
import com.page6.entity.Board;
import com.page6.entity.Comment;
import com.page6.entity.Heart;
import com.page6.repository.BoardRepository;
import com.page6.repository.HeartRepository;
import com.page6.service.BoardService;
import com.page6.service.CommentService;
import com.page6.service.HeartService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ApplicationTests {

    @Autowired
    private BoardService boardService;

    @DisplayName("보드 테스트")
    @Test
    void 보드_테스트_시작(@Autowired CommentService commentService) {
        List<CommentFormDto> list = commentService.getCommentList(1L);
        for(int i = 0; i < list.size(); i++)
            System.out.println(list.get(i).toString());
        System.out.println("테스트 끝");
    }
      //  더미데이터 만들기
//    @DisplayName("게시글 더미데이터")
//    @Test
//    void 게시글_더미데이터() {
//        Board board = new Board("hello", "hello");
//        for(int i = 0; i < 500; i++) {
//            board = new Board("hello " + i, "hello " + i);
//            boardService.write(board, "test@test");
//        }
//    }

    @DisplayName("좋아요 추가 테스트")
    @Test
    void 좋아요_추가(@Autowired HeartService heartService) {
        heartService.addHeart(1L, "test@test");
    }

    @DisplayName("좋아요 조회 테스트")
    @Test
    @Transactional
    void 좋아요_조회(@Autowired HeartRepository heartRepository) {
        heartRepository.deleteById(1L);
        List<Heart> heartList = heartRepository.findAll();
        for(int i = 0; i < heartList.size(); i++)
        System.out.println(heartList.get(i).toString());
        System.out.println("테스트 끝");
    }

//    @Test
//    @DisplayName("다중 해시태그 테스트")
//    void 해시태그_조회_테스트(@Autowired BoardRepository boardRepository) {
//        BoardSpecification boardSpecification = new BoardSpecification(boardRepository);
//        System.out.println("///////////////////////////////////테스트 시작///////////////////////////////////");
//        String[] tags = {"해태", "해테"};
//        String[] excludedTags = {};
//        List<Board> list = boardSpecification.findByTagsContaining(tags, excludedTags);
//        for(int i = 0; i < list.size(); i++)
//            System.out.println(list.get(i).toString());
//        System.out.println("///////////////////////////////////테스트 종료///////////////////////////////////");
//    }

//    @DisplayName("정렬 테스트")
//    @Test
//    void 정렬() {
//        List<Board> nameSort = boardService.findAllByOrderByName();
//        for(int i = 0; i < nameSort.size(); i++)
//            System.out.println(nameSort.get(i).toString());
//
//    }


//    @DisplayName("조회수 테스트")
//    @Test
//    void 조회수_테스트() {
//        boardService.viewCntUpdate(2);
//        System.out.println("테스트 끝");
//    }

//    @Test
//    @DisplayName("해시태그 서치 테스트")
//    void 서치_테스트() {
//        System.out.println("///////////////////////////////////테스트 시작///////////////////////////////////");
//        String[] tags = {"해테", "해태"};
//        List<Board> list = boardService.searchBoardByTags(tags);
//        System.out.println("///////////////////////////////////출력시작///////////////////////////////////");
//        for(int i = 0; i < list.size(); i++)
//            System.out.println(list.get(i).toString());
//        System.out.println("///////////////////////////////////테스트 끝///////////////////////////////////");
//    }
}
