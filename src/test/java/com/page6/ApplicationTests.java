package com.page6;

import com.page6.controller.BoardController;
import com.page6.dto.BoardDto;
import com.page6.dto.CommentFormDto;
import com.page6.entity.Board;
import com.page6.entity.Comment;
import com.page6.service.BoardService;
import com.page6.service.CommentService;
import com.page6.service.HeartService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    void 좋아요_조회(@Autowired HeartService heartService) {
        boolean flag = heartService.heartFlag(1L, "test@test");
        System.out.println("flag값=" + flag);
    }

    @DisplayName("검색 테스트")
    @Test
    void 검색_테스트() {
//        String keyword = "불곰";
//        List<Board> list = boardService.searchByWriter(keyword);
//        for(int i = 0; i < list.size(); i++)
//            System.out.println(list.get(i).toString());
    }
//    @DisplayName("조회수 테스트")
//    @Test
//    void 조회수_테스트() {
//        boardService.viewCntUpdate(2);
//        System.out.println("테스트 끝");
//    }
}
