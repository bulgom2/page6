package com.page6.exception;

import com.page6.exception.InvalidAccessException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
//    @ExceptionHandler(InvalidAccessException.class)
//    public String handleInvalidAccessException(RedirectAttributes redirectAttributes, InvalidAccessException ex) {
//        String message = ex.getMessage();
//        redirectAttributes.addFlashAttribute("message", message);
//        return "redirect:/errorpage";
//    }
    @ExceptionHandler(InvalidAccessException.class)
    public String handleInvalidAccessException(Model model) {
        model.addAttribute("message", "잘못된 접근입니다. 이전 페이지로 이동합니다.");
        return "exception/errorpage";
    }
}

