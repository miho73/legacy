package com.github.miho73.legacy.service;

import com.github.miho73.legacy.dto.Articles;
import com.github.miho73.legacy.dto.Files;
import com.github.miho73.legacy.repository.ArticlesRepository;
import com.github.miho73.legacy.repository.FilesRepository;
import com.github.miho73.legacy.utils.SHA;
import jakarta.annotation.PostConstruct;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.List;

@Service
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

    public void saveFile(MultipartFile file, String hash, String fileName) throws IOException {
        Files fileObj = new Files();
        fileObj.setData(file.getBytes());
        fileObj.setFileHash(hash);
        fileObj.setFileName(fileName);
        filesRepository.save(fileObj);
    }

    private JSONArray putInJsonArray(List<Articles> articles) {
        JSONArray ret = new JSONArray();
        articles.forEach(e -> {
            JSONObject toAdd = new JSONObject();
            toAdd.put("name", e.getName());
            toAdd.put("explain", e.getExplain());
            toAdd.put("tags", e.getTags());
            toAdd.put("hash", e.getFileHash());
            ret.add(toAdd);
        });
        return ret;
    }

    public JSONArray getArticleByUidRange(int from, int to) {
        List<Articles> articles = articleRepository.findByUidInRange(from, to);
        return putInJsonArray(articles);
    }

    public JSONArray getArticleFromBack(int len) {
        List<Articles> articles = articleRepository.getWithCountDesc(len);
        return putInJsonArray(articles);
    }

    public JSONArray searchArticleByQuery(String query) {
        List<Articles> articles = articleRepository.searchByQuery(query);
        return putInJsonArray(articles);
    }
}
