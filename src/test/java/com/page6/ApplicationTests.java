package com.page6;

import com.page6.controller.BoardController;
import com.page6.dto.BoardDto;
import com.page6.entity.Board;
import com.page6.service.BoardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ApplicationTests {
    @Autowired
    private BoardController boardController;

    @Autowired
    private BoardService boardService;

    @DisplayName("보드 테스트")
    @Test
    void 보드_테스트_시작() {
        List<BoardDto> boardList = boardService.getBoardList();
        for(int i = 0; i < boardList.size(); i++) {
            System.out.println(boardList.get(i).toString());
        }
        System.out.println("테스트 끝");
    }

//    @DisplayName("조회수 테스트")
//    @Test
//    void 조회수_테스트() {
//        boardService.viewCntUpdate(2);
//        System.out.println("테스트 끝");
//    }
}
