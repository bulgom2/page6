package com.page6.controller;

import com.page6.exception.InvalidAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ExceptionController {
    @GetMapping("/errorpage")
    public String handleErrorPage(Model model) {
        String message = (String) model.getAttribute("message");
        model.addAttribute("message", message);
        return "exception/errorpage";
    }
}
