package com.aivle.bookapp.repository;

import com.aivle.bookapp.domain.Episode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {
    List<Episode> findByTitle(String title);
    List<Episode> findByAuthor(String author);
    List<Episode> findByTitleContaining(String keyword);
    List<Episode> findByTitleAndAuthor(String title, String author);
}

