package com.github.miho73.legacy.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@Slf4j
@RequestMapping("/login")
public class LoginContoller {
    @GetMapping("")
    public String responseLoginPage(Model model) {
        return "index";
    }

    @PostMapping(
            value = "/auth",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public String performLogin(
            HttpServletResponse response,
            @RequestBody String id, @RequestBody String pwd,
            HttpSession session
    ) throws IOException {
        log.info(id, pwd);
        session.setAttribute("priv", 1);
        response.sendRedirect("/");
        return "";
    }
}
