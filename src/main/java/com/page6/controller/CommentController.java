package com.page6.controller;

import com.page6.dto.CommentFormDto;
import com.page6.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/comment")
public class CommentController {
    @Autowired
    private final CommentService commentService;

    //일반 댓글 등록
    @PostMapping("/add")
    public String commentAdd(CommentFormDto dto, Principal principal){
        dto.setWriter(principal.getName());
        dto.setDepth(0);
        dto.setGroup(0L);
        commentService.commentAdd(dto);
        return "redirect:/board/" + dto.getBid();
    }

    //일반 댓글 등록
    @PostMapping("/add/recomment")
    public String recommentAdd(CommentFormDto dto, Principal principal){
        dto.setWriter(principal.getName());
        dto.setDepth(1);
        commentService.commentAdd(dto);
        return "redirect:/board/" + dto.getBid();
    }

    //댓글 리스트 출력
//    @GetMapping("/list/{bid}")
//    public void commentList(@PathVariable("bid") long bid, Model model) {
//        List<CommentFormDto> list = commentService.getCommentList(bid);
//        model.addAttribute("commentList", list);
//    }

}
