package io.todak.study.springbootjpawebapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model) {
        model.addAttribute("data", "hello");
        
        //TODO : 06.H2 데이터베이스 설치부터 수강 ㄱ
        
        return "hello";
    }

}
