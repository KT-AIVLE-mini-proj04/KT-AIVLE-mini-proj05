package com.aivle.bookapp.controller;

import com.aivle.bookapp.domain.Episode;
import com.aivle.bookapp.service.EpisodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EpisodeController {
    private final EpisodeService episodeService;

    @GetMapping("/episodes/{id}")
    public Episode getEpisode(@PathVariable Long id) {return episodeService.findById(id);}

    @GetMapping("/episodes")
    public List<Episode> getAll() {return episodeService.findAll();}

    @DeleteMapping("/episodes/{id}")
    public ResponseEntity<Void> deleteEpisode(@PathVariable Long id) {
        episodeService.deleteEpisode(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/episodes/count")
    public long getCount() {return episodeService.count();}

    @GetMapping("/episodes/search/title")
    public List<Episode> searchByTitle(@RequestParam String title) {
        return episodeService.searchByTitle(title);
    }

    @GetMapping("/episodes/search")
    public List<Episode> searchByKeyword(@RequestParam String keyword) {
        return episodeService.searchByKeyword(keyword);
    }

    @GetMapping("/episodes/search/detail")
    public List<Episode> searchByTitleAndAuthor(@RequestParam String title, @RequestParam String author) {
        return episodeService.searchByTitleAndAuthor(title, author);
    }

    @GetMapping("/episodes/search/author")
    public List<String> authorGetTitle(@RequestParam String author) {
        return episodeService.authorGetTitle(author);
    }

    @GetMapping("/episodes/page")
    public Page<Episode> getPage(@RequestParam int page, @RequestParam int size, @RequestParam String sortBy) {
        return episodeService.getPage(page, size, sortBy);
    }

    @PostMapping("/episodes")
    public ResponseEntity<Episode> createEpisode(@Valid @RequestBody Episode episode) {
        Episode saved = episodeService.create(episode);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PatchMapping("/episodes/{id}")
    public Episode updateEpisode(@PathVariable Long id, @RequestBody Episode episode) {
        return episodeService.update(id, episode);
    }
}

