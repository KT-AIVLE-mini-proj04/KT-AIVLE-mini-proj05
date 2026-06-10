package com.aivle.bookapp.service;

import com.aivle.bookapp.domain.Episode;
import com.aivle.bookapp.exception.EpisodeNotFoundException;
import com.aivle.bookapp.repository.EpisodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public List<Episode> findAll() {return episodeRepository.findAll();}

    @Transactional
    public void deleteEpisode(Long id) {
        if (episodeRepository.existsById(id)) {episodeRepository.deleteById(id);}
        else {throw new RuntimeException("id가 " + id + "인 도서가 없습니다.");}
    }

    @Transactional(readOnly = true)
    public long count() {return episodeRepository.count();}

    @Transactional(readOnly = true)
    public List<Episode> searchByTitle(String title) {return episodeRepository.findByTitle(title);}

    @Transactional(readOnly = true)
    public List<Episode> searchByKeyword(String keyword) {
        return episodeRepository.findByTitleContaining(keyword);
    }

    @Transactional(readOnly = true)
    public List<Episode> searchByTitleAndAuthor(String title, String author) {
        return episodeRepository.findByTitleAndAuthor(title, author);
    }

    @Transactional(readOnly = true)
    public List<String> authorGetTitle(String author) {
        List<Episode> episodes = episodeRepository.findByAuthor(author);
        return episodes.stream().map(episode -> episode.getTitle()).toList();
    }

    @Transactional(readOnly = true)
    public Page<Episode> getPage(int page, int size, String sortBy) {
        Sort sort = Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size,sort);
        return episodeRepository.findAll(pageable);
    }

    @Transactional
    public Episode create(Episode episode) {return episodeRepository.save(episode);}

    @Transactional
    public Episode update(Long id, Episode episode) {
        Episode existing = findById(id);
        if (episode.getTitle() != null) {existing.setTitle(episode.getTitle());}
        if (episode.getAuthor() != null) {existing.setAuthor(episode.getAuthor());}
        return episodeRepository.save(existing);
    }
}

