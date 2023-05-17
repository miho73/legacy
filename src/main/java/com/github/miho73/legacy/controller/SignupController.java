package com.github.miho73.legacy.controller;

import com.github.miho73.legacy.dto.User;
import com.github.miho73.legacy.service.AuthService;
import com.github.miho73.legacy.utils.RestResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/signup")
public class SignupController {
    @Autowired
    AuthService authService;

    @GetMapping("")
    public String responseSignupPage() {
        return "index";
    }

    @PostMapping(
            value = "/test/id-duplication",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public String testIdDuplication(@RequestBody Map<String, String> body) {
        boolean ok = authService.existsUserById(body.get("id"));
        return RestResponse.restResponse(HttpStatus.OK, ok ? 0 : 1);
    }

    @PostMapping(
            value = "/post",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public String createLegacyId(@Valid @RequestBody User user, HttpServletResponse res
    ) {
        User created = authService.createUser(user.getName(), user.getCode(), user.getId(), user.getPwd());
        return RestResponse.restResponse(HttpStatus.CREATED, created.getId());
    }

    @GetMapping("/activation")
    public String responseActivation() {
        return "index";
    }

    @PostMapping(
            value = "/activation/check",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public String checkActive(HttpServletResponse response, @RequestBody Map<String, Object> body) {
        if(!body.containsKey("id") || !body.containsKey("pwd")) {
            response.setStatus(400);
            return RestResponse.restResponse(HttpStatus.BAD_REQUEST);
        }

        int active = authService.checkActiveStatus(body.get("id").toString(), body.get("pwd").toString());
        return RestResponse.restResponse(HttpStatus.OK, active);
    }
}
