package com.aivle.bookapp.controller;

import com.aivle.bookapp.domain.Episode;
import com.aivle.bookapp.service.EpisodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/episodes")
public class EpisodeController {
    private final EpisodeService episodeService;

    @GetMapping("/{id}")
    public ResponseEntity<Episode> getEpisode(@PathVariable Long id) {
        return ResponseEntity.ok(episodeService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<Episode>> getEpisodesByBookId(@RequestParam Long bookId) {
        return ResponseEntity.ok(episodeService.findByBookId(bookId));
    }

    @PostMapping
    public ResponseEntity<Episode> createEpisode(@Valid @RequestBody Episode episode) {
        Episode saved = episodeService.create(episode);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Episode> updateEpisode(@PathVariable Long id, @RequestBody Episode episode) {
        return ResponseEntity.ok(episodeService.update(id, episode));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteEpisode(@PathVariable Long id) {
        episodeService.deleteEpisode(id);
        return ResponseEntity.ok(Map.of("message", "에피소드가 삭제되었습니다."));
    }

    @PostMapping("/{id}/tts")
    public ResponseEntity<Map<String, String>> updateTtsPath(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String ttsPath = payload.get("ttsPath");
        episodeService.updateTtsPath(id, ttsPath);
        return ResponseEntity.ok(Map.of("message", "에피소드의 TTS가 등록되었습니다."));
    }
}


