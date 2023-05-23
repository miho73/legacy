package com.github.miho73.legacy.repository;

import com.github.miho73.legacy.dto.Articles;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticlesRepository extends JpaRepository<Articles, Integer> {
    @Query(
            value = "SELECT * FROM data.articles WHERE uid >= :from AND uid <= :to",
            nativeQuery = true
    )
    List<Articles> findByUidInRange(
            @Param("from") int from,
            @Param("to") int to
    );

    @Query(
            value = "SELECT * FROM data.articles ORDER BY uid DESC LIMIT :cnt",
            nativeQuery = true
    )
    List<Articles> getWithCountDesc(
            @Param("cnt") int cnt
    );
    @Query(
            value = "SELECT * FROM data.articles ORDER BY uid ASC LIMIT :cnt",
            nativeQuery = true
    )
    List<Articles> getWithCountAsc(
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


}
