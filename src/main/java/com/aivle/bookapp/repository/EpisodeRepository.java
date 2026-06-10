package com.aivle.bookapp.repository;

import com.aivle.bookapp.domain.Episode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {
    List<Episode> findByBookId(Long bookId);
}

