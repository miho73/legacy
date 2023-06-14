package com.github.miho73.legacy.repository;

import com.github.miho73.legacy.dto.Articles;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface ArticlesRepository extends JpaRepository<Articles, Integer> {
    @Query(
            value = "SELECT * FROM data.articles ORDER BY uid DESC LIMIT :cnt",
            nativeQuery = true
    )
    List<Articles> getWithCountDesc(
            @Param("cnt") int cnt
    );

    long count();

    @Query(
            value = "SELECT * FROM data.articles WHERE name LIKE %:query% OR explain LIKE %:query% ORDER BY uid DESC",
            nativeQuery = true
    )
    List<Articles> searchByQuery(
            @Param("query") String query
    );

    @Query(
            value = "UPDATE data.articles SET downs=downs+1, last_down=:lstDwn WHERE file_hash=:hash",
            nativeQuery = true
    )
    @Modifying
    void downloadArticle(
            @Param("hash") String hash,
            @Param("lstDwn") Timestamp lstDwn
    );
}
