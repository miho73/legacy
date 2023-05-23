package com.github.miho73.legacy.controller;

import com.github.miho73.legacy.dto.User;
import com.github.miho73.legacy.exception.LegacyException;
import com.github.miho73.legacy.service.AuthService;
import com.github.miho73.legacy.service.SessionService;
import com.github.miho73.legacy.utils.RestResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/login")
public class LoginController {
    @Autowired
    AuthService authService;

    @Autowired
    SessionService sessionService;

    @GetMapping("")
    public String responseLoginPage() {
        return "index";
    }

    @PostMapping(
            value = "/auth",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public String performLogin(
            HttpServletResponse response,
            @RequestBody Map<String, String> body,
            HttpSession session
    ) {
        if(!body.containsKey("id") || !body.containsKey("pwd")) {
            response.setStatus(400);
            return RestResponse.restResponse(HttpStatus.BAD_REQUEST);
        }

        User user = null;
        try {
            user = authService.getUserById(body.get("id"));
        } catch (LegacyException e) {
            return RestResponse.restResponse(HttpStatus.OK, 0);
        }
        int auth = authService.authenticate(body.get("pwd"), user);
        int active = authService.checkActiveStatus(body.get("id"), body.get("pwd"));
        if(auth == 0) {
            if(active == 0) {
                session.setAttribute("priv", user.getPrivilege());
                session.setAttribute("uuid", user.getUid());
                return RestResponse.restResponse(HttpStatus.OK, 1);
            }
            else {
                return RestResponse.restResponse(HttpStatus.OK, 2);
            }
        }
        else {
            return RestResponse.restResponse(HttpStatus.OK, 0);
        }
    }

    @GetMapping("/logout")
    public String performLogout(HttpSession session) {
        session.setAttribute("priv", 0);
        return "redirect:/";
    }

    @GetMapping("authorize")
    @ResponseBody
    public String authorize(HttpSession session, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        boolean priv =sessionService.checkPrivilege(session, sessionService.privilegeOf(true, false));
        return RestResponse.restResponse(HttpStatus.OK, priv);
    }
}
