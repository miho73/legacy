package com.github.miho73.legacy.service;

import com.github.miho73.legacy.dto.Articles;
import com.github.miho73.legacy.dto.Files;
import com.github.miho73.legacy.repository.ArticlesRepository;
import com.github.miho73.legacy.repository.FilesRepository;
import com.github.miho73.legacy.utils.SHA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

@Service
public class ArticleService {
    @Autowired
    SHA sha;

    @Autowired
    ArticlesRepository articleRepository;
    @Autowired
    FilesRepository filesRepository;

    public boolean existsHash(String hash) {
        return filesRepository.findByFileHash(hash).isEmpty();
    }

    public String calculateHash(MultipartFile file) throws IOException, NoSuchAlgorithmException {
        return sha.MD5(file.getBytes());
    }

    public void saveArticle(String hash, String name, String explain, int tags, int uploadedBy) {
        Articles article = new Articles();
        article.setName(name);
        article.setDownloads(0);
        article.setExplain(explain);
        article.setTags(tags);
        article.setFileHash(hash);
        article.setDateUp(new Timestamp(System.currentTimeMillis()));
        article.setUploadedBy(uploadedBy);
        articleRepository.save(article);
    }

    public void saveFile(MultipartFile file, String hash) throws IOException {
        Files fileObj = new Files();
        fileObj.setData(file.getBytes());
        fileObj.setFileHash(hash);
        filesRepository.save(fileObj);
    }
}
