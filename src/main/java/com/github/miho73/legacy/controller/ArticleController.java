package com.github.miho73.legacy.controller;

import com.github.miho73.legacy.dto.UploadForm;
import com.github.miho73.legacy.service.ArticleService;
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

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Controller
@RequestMapping("/upload")
@Slf4j
public class ArticleController {
    @Autowired
    SessionService sessionService;

    @Autowired
    ArticleService articleService;

    @GetMapping("")
    public String responseUpload(HttpSession session) {
        if(!sessionService.checkPrivilege(session, sessionService.privilegeOf(true, false))) {
            return "redirect:/login";
        }
        return "index";
    }

    /**
     * 0: forbidden, 1: duplicated hash, 2: too large
     */
    @PostMapping(
            value = "/cdn/post",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public String saveArticle(
            HttpSession session,
            HttpServletResponse response,
            @ModelAttribute UploadForm uploadForm) throws IOException {
        if(!sessionService.checkPrivilege(session, sessionService.privilegeOf(true, false))) {
            response.setStatus(403);
            return RestResponse.restResponse(HttpStatus.FORBIDDEN, 0);
        }
        // 500 MB
        if(uploadForm.getFile().getSize() > 500000000) {
            response.setStatus(400);
            return RestResponse.restResponse(HttpStatus.BAD_REQUEST, 2);
        }
        try {
            String hash = articleService.calculateHash(uploadForm.getFile());

            if(!articleService.existsHash(hash)) {
                response.setStatus(400);
                return RestResponse.restResponse(HttpStatus.BAD_REQUEST, 1);
            }

            articleService.saveArticle(hash, uploadForm.getName(), uploadForm.getExplain(), uploadForm.getTags(), (int)session.getAttribute("uuid"));
            articleService.saveFile(uploadForm.getFile(), hash);
            return RestResponse.restResponse(HttpStatus.OK, hash);
        } catch (NoSuchAlgorithmException e) {
            log.error("NoSuchAlgorithmException", e);
            response.setStatus(500);
            return RestResponse.restResponse(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
