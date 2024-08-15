package com.example.demo.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(Model model) {
        // Chèn một chuỗi vào biến dynamicContent
        model.addAttribute("dynamicContent", "This is dynamically added content!");

        // Chèn một danh sách vào biến items
        List<String> items = Arrays.asList("chó", "mèo", "Item 3");
        model.addAttribute("items", items);

        return "x"; // Trả về file x.html
    }
}
