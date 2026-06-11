package com.aivle.bookapp.service;

import com.aivle.bookapp.domain.Episode;
import com.aivle.bookapp.dto.EpisodeRequestDto;
import com.aivle.bookapp.dto.EpisodeUpdateRequest;
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
    public Episode create(EpisodeRequestDto dto) {
        Episode episode = new Episode();
        episode.setBookId(dto.getBookId());
        episode.setUsersId(dto.getUsersId());
        episode.setEpisodeTitle(dto.getEpisodeTitle());
        episode.setEpisodeIndex(dto.getEpisodeIndex());
        episode.setContent(dto.getContent());
        episode.setView(0);
        return episodeRepository.save(episode);
    }

    @Transactional
    public Episode update(Long id, EpisodeUpdateRequest dto) {
        Episode existing = findById(id);
        if (dto.getEpisodeTitle() != null) {
            existing.setEpisodeTitle(dto.getEpisodeTitle());
        }
        if (dto.getEpisodeIndex() != null) {
            existing.setEpisodeIndex(dto.getEpisodeIndex());
        }
        if (dto.getContent() != null) {
            existing.setContent(dto.getContent());
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
