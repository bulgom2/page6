package com.page6.controller;

import com.page6.dto.BoardDto;
import com.page6.service.BoardService;
import com.page6.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private final MemberService memberService;
    private final BoardService boardService;


    //휴지통 기능
    @GetMapping({"/", "/{page}"})
    public String recycleList(Model model, Principal principal, @RequestParam(defaultValue = "1") int page) {
        //관리자가 아니면 접근 금지
        String email = principal.getName();
        if(!memberService.isAdmin(email)) {
            model.addAttribute("message", "잘못된 접근입니다. 이전 페이지로 이동합니다.");
            return "exception/errorpage";
        }

        // 한 페이지당 보여줄 게시물 수
        int size = 10;
        page = page < 1 ? 1 : page;

        // 페이지 번호는 0부터 시작하므로 1을 빼준다
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());

        // 리스트 불러오기
        Page<BoardDto> recyclePage = boardService.getDeletedBoard(pageable);

        // 페이징 번호
        int nowPage = recyclePage.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage;
        int lastPage = recyclePage.getTotalPages();

        int maxPage = 10; // 최대 페이지 수
        if (recyclePage.getTotalPages() < maxPage) {
            endPage = recyclePage.getTotalPages();
        } else {
            endPage = Math.min(nowPage + 5, recyclePage.getTotalPages());
            if (nowPage < 5)
                endPage = maxPage;
            else if (nowPage < maxPage - 5)
                endPage = maxPage;
        }

        if (nowPage == 0) startPage = 1;

        model.addAttribute("recycleList", recyclePage);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("lastPage", lastPage);
        return "board/admin";
    }



    //복구 요청
    @GetMapping("/undelete/{id}")
    public String boardUndelete(@PathVariable("id") Long id, Model model, Principal principal) {
        //TODO: 접근하려는 사람이 작성자인지 확인하기
        String email = principal.getName();
        if(!memberService.isAdmin(email)) {
            model.addAttribute("message", "잘못된 접근입니다. 이전 페이지로 이동합니다.");
            return "exception/errorpage";
        }

        boardService.boardUndelete(id);
        return "redirect:/";
    }

}
