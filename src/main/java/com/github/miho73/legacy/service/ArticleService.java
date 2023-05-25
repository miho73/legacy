package com.github.miho73.legacy.service;

import com.github.miho73.legacy.dto.Articles;
import com.github.miho73.legacy.dto.Files;
import com.github.miho73.legacy.repository.ArticlesRepository;
import com.github.miho73.legacy.repository.FilesRepository;
import com.github.miho73.legacy.utils.SHA;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
@Slf4j
public class ArticleService {
    @Autowired
    SHA sha;

    @Autowired
    ArticlesRepository articleRepository;
    @Autowired
    FilesRepository filesRepository;

    private long ARTICLE_COUNT;
    final int ARTICLES_PER_PAGE = 20;

    @PostConstruct
    public void init() {
        ARTICLE_COUNT = articleRepository.count();
    }

    public boolean existsHash(String hash) {
        return filesRepository.findByFileHash(hash).isEmpty();
    }

    public String calculateHash(MultipartFile file) throws IOException, NoSuchAlgorithmException {
        log.debug("Hash: "+sha.SHA256(file.getBytes()));
        return sha.SHA256(file.getBytes());
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

    public void saveFile(MultipartFile file, String hash, String fileName, long size) throws IOException {
        Files fileObj = new Files();
        fileObj.setData(file.getBytes());
        fileObj.setFileHash(hash);
        fileObj.setFileName(fileName);
        fileObj.setSize(size);
        filesRepository.save(fileObj);
    }

    private JSONArray putInJsonArray(List<Articles> articles) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        JSONArray ret = new JSONArray();
        articles.forEach(e -> {
            JSONObject toAdd = new JSONObject();
            toAdd.put("name", e.getName());
            toAdd.put("explain", e.getExplain());
            toAdd.put("tags", e.getTags());
            toAdd.put("hash", e.getFileHash());
            toAdd.put("date", sdf.format(e.getDateUp()));
            ret.add(toAdd);
        });
        return ret;
    }

    public JSONArray getArticleFromBack(int len) {
        List<Articles> articles = articleRepository.getWithCountDesc(len);
        return putInJsonArray(articles);
    }

    public JSONArray searchArticleByQuery(String query) {
        List<Articles> articles = articleRepository.searchByQuery(query);
        return putInJsonArray(articles);
    }

    public void downloadedArticle(String hash) {
        articleRepository.downloadArticle(hash, new Timestamp(System.currentTimeMillis()));
    }
}
