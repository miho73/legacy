package com.github.miho73.legacy.controller;

import com.github.miho73.legacy.service.ArticleService;
import com.github.miho73.legacy.service.SessionService;
import com.github.miho73.legacy.utils.RestResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    SessionService sessionService;

    @Autowired
    ArticleService articleService;

    final int ARTICLES_PER_REQUEST = 100;

    @GetMapping(
            value = "/get/latest",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public String retrieveLatestArticle(
            HttpSession session,
            HttpServletResponse response,
            @RequestParam("length") int len
    ) {
        if(!sessionService.checkPrivilege(session, sessionService.privilegeOf(true, false))) {
            response.setStatus(403);
            return RestResponse.restResponse(HttpStatus.FORBIDDEN, 1);
        }

        if(len <= 0 || len > ARTICLES_PER_REQUEST) {
            response.setStatus(400);
            return RestResponse.restResponse(HttpStatus.BAD_REQUEST, 2);
        }

        return RestResponse.restResponse(HttpStatus.OK, articleService.getArticleFromBack(len));
    }

    @GetMapping(
            value = "/get/search",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public String searchArticles(
            HttpSession session,
            HttpServletResponse response,
            @RequestParam("query") String query
    ) {
        if(!sessionService.checkPrivilege(session, sessionService.privilegeOf(true, false))) {
            response.setStatus(403);
            return RestResponse.restResponse(HttpStatus.FORBIDDEN, 1);
        }

        if(query.isEmpty() || query.length() >= 50) {
            response.setStatus(400);
            return RestResponse.restResponse(HttpStatus.BAD_REQUEST, 2);
        }

        return RestResponse.restResponse(HttpStatus.OK, articleService.searchArticleByQuery(query));
    }
}
