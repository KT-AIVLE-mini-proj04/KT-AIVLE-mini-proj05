package com.aivle.bookapp.service;

import com.aivle.bookapp.domain.Episode;
import com.aivle.bookapp.exception.EpisodeNotFoundException;
import com.aivle.bookapp.repository.EpisodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EpisodeService {
    private final EpisodeRepository episodeRepository;

    @Transactional(readOnly = true)
    public Episode findById(Long id) {
        return episodeRepository.findById(id).orElseThrow(()
                -> new EpisodeNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Episode> findByBookId(Long bookId) {
        return episodeRepository.findByBookId(bookId);
    }

    @Transactional
    public void deleteEpisode(Long id) {
        if (episodeRepository.existsById(id)) {
            episodeRepository.deleteById(id);
        } else {
            throw new EpisodeNotFoundException(id);
        }
    }

    @Transactional
    public Episode create(Episode episode) {
        return episodeRepository.save(episode);
    }

    @Transactional
    public Episode update(Long id, Episode episode) {
        Episode existing = findById(id);
        if (episode.getEpisodeTitle() != null) {
            existing.setEpisodeTitle(episode.getEpisodeTitle());
        }
        if (episode.getEpisodeIndex() != null) {
            existing.setEpisodeIndex(episode.getEpisodeIndex());
        }
        if (episode.getContent() != null) {
            existing.setContent(episode.getContent());
        }
        if (episode.getTtsPath() != null) {
            existing.setTtsPath(episode.getTtsPath());
        }
        return episodeRepository.save(existing);
    }

    @Transactional
    public Episode updateTtsPath(Long id, String ttsPath) {
        Episode episode = findById(id);
        episode.setTtsPath(ttsPath);
        return episodeRepository.save(episode);
    }
}
