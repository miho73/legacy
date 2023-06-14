package com.github.miho73.legacy.controller;

import com.github.miho73.legacy.dto.Files;
import com.github.miho73.legacy.repository.FilesRepository;
import com.github.miho73.legacy.service.ArticleService;
import com.github.miho73.legacy.service.SessionService;
import com.github.miho73.legacy.utils.RestResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@Slf4j
@RequestMapping("/cdn")
public class CdnController {
    @Autowired
    FilesRepository filesRepository;
    @Autowired
    ArticleService articleService;

    @Autowired
    SessionService sessionService;

    @GetMapping(
            value = "/get/{hash}",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    @Transactional
    public void downloadFile(
            @PathVariable("hash") String hash,
            HttpServletResponse response,
            HttpSession session
    ) throws IOException {
        try {
            if(!sessionService.checkPrivilege(session, sessionService.privilegeOf(true, false))) {
                response.setStatus(403);
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().print(RestResponse.restResponse(HttpStatus.FORBIDDEN, 1));
                response.flushBuffer();
                return;
            }

            if(!filesRepository.existsByFileHash(hash)) {
                response.setStatus(400);
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().print(RestResponse.restResponse(HttpStatus.BAD_REQUEST,2));
                response.flushBuffer();
                return;
            }

            Files fileDto = filesRepository.findByFileHash(hash).get(0);
            byte[] file = fileDto.getData();
            log.info("filename: "+fileDto.getFileName());

            String fileName = URLEncoder.encode(fileDto.getFileName(), StandardCharsets.UTF_8).replaceAll("\\+", "%20");

            response.setContentType("application/octet-stream; charset=utf-8");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition","attachment; filename*=UTF-8''"+fileName);

            articleService.downloadedArticle(hash);
            InputStream is = new ByteArrayInputStream(file);
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();

        } catch (Exception e) {
            log.error("Cannot serve file", e);
            response.setStatus(500);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(RestResponse.restResponse(HttpStatus.INTERNAL_SERVER_ERROR,3));
            response.flushBuffer();
            throw new RuntimeException(e);
        }
    }
}
