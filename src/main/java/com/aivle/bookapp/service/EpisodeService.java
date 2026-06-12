package com.aivle.bookapp.service;

import com.aivle.bookapp.domain.Episode;
import com.aivle.bookapp.dto.EpisodeRequestDto;
import com.aivle.bookapp.dto.EpisodeUpdateRequest;
import com.aivle.bookapp.exception.BookNotFoundException;
import com.aivle.bookapp.exception.EpisodeNotFoundException;
import com.aivle.bookapp.repository.BookRepository;
import com.aivle.bookapp.repository.EpisodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EpisodeService {
    private final EpisodeRepository episodeRepository;
    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public Episode findById(Long id) {
        return episodeRepository.findById(id).orElseThrow(()
                -> new EpisodeNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Episode> findByBookId(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundException(bookId);
        }
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
        if (!bookRepository.existsById(dto.getBookId())) {
            throw new BookNotFoundException(dto.getBookId());
        }
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
        if (dto.getEpisodeTitle() == null && dto.getEpisodeIndex() == null && dto.getContent() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정할 필드를 하나 이상 입력해야 합니다.");
        }
        if (dto.getEpisodeTitle() != null && dto.getEpisodeTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "에피소드 제목은 빈 값일 수 없습니다.");
        }
        if (dto.getContent() != null && dto.getContent().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "에피소드 내용은 빈 값일 수 없습니다.");
        }
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
