package com.example.demo.controller;

import com.example.demo.dto.CoupleResponse;
import com.example.demo.service.CoupleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ChristmasController {
    
    private final CoupleService coupleService;
    
    @GetMapping("/")
    public String home(Model model) {
        try {
            CoupleResponse couple = coupleService.getCouple();
            model.addAttribute("couple", couple);
        } catch (RuntimeException e) {
            // 커플 정보가 없으면 기본값 설정
            model.addAttribute("couple", null);
        }
        return "christmas";
    }
}
