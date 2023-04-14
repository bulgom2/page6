package com.page6.controller;

import com.page6.dto.CommentFormDto;
import com.page6.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/comment")
public class CommentController {
    @Autowired
    private final CommentService commentService;

    //댓글 등록
    @PostMapping("/add")
    public String commentAdd(CommentFormDto dto, Principal principal){
        dto.setWriter(principal.getName());
        dto.setDepth(0);
        dto.setGroup(0);
        commentService.commentAdd(dto);
        return "redirect:/board/" + dto.getBid();
    }



}
