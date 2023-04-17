package com.page6.controller;

import com.page6.entity.Board;
import com.page6.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/like")
public class HeartController {
    private final HeartService heartService;

    @PostMapping("/add")
    public String addLike(@RequestParam("bid") Long bid, Principal principal){
        String email = principal.getName();
        heartService.addHeart(bid, email);
        return "redirect:/board/" + bid;
    }
}
