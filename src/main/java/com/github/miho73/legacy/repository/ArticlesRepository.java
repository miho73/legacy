package com.github.miho73.legacy.repository;

import com.github.miho73.legacy.dto.Articles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticlesRepository extends JpaRepository<Articles, Integer> {
}
