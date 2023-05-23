package com.github.miho73.legacy.repository;

import com.github.miho73.legacy.dto.Files;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilesRepository extends JpaRepository<Files, Integer> {
    List<Files> findByFileHash(String id);

    boolean existsByFileHash(String hash);
}
