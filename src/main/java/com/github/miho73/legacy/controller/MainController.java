package com.github.miho73.legacy.controller;

import com.github.miho73.legacy.service.SessionService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class MainController {
    @Autowired
    SessionService sessionService;

    @GetMapping("/")
    public String responseIndex(HttpServletResponse res, HttpSession session) {
        if(sessionService.checkPrivilege(session, sessionService.privilegeOf(true, false))) {
            return "index";
        }
        else {
            return "redirect:/login";
        }
    }
}
