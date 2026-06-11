package com.aivle.bookapp.controller;

import com.aivle.bookapp.dto.EpisodeRequestDto;
import com.aivle.bookapp.dto.EpisodeResponseDto;
import com.aivle.bookapp.dto.EpisodeUpdateRequest;
import com.aivle.bookapp.dto.TtsUpdateRequest;
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
    public ResponseEntity<EpisodeResponseDto> getEpisode(@PathVariable Long id) {
        return ResponseEntity.ok(new EpisodeResponseDto(episodeService.findById(id)));
    }

    @GetMapping
    public ResponseEntity<List<EpisodeResponseDto>> getEpisodesByBookId(@RequestParam Long bookId) {
        List<EpisodeResponseDto> responses = episodeService.findByBookId(bookId)
                .stream()
                .map(EpisodeResponseDto::new)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<EpisodeResponseDto> createEpisode(@Valid @RequestBody EpisodeRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new EpisodeResponseDto(episodeService.create(dto)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EpisodeResponseDto> updateEpisode(@PathVariable Long id, @RequestBody EpisodeUpdateRequest dto) {
        return ResponseEntity.ok(new EpisodeResponseDto(episodeService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteEpisode(@PathVariable Long id) {
        episodeService.deleteEpisode(id);
        return ResponseEntity.ok(Map.of("message", "에피소드가 삭제되었습니다."));
    }

    @PostMapping("/{id}/tts")
    public ResponseEntity<Map<String, String>> updateTtsPath(@PathVariable Long id, @Valid @RequestBody TtsUpdateRequest dto) {
        episodeService.updateTtsPath(id, dto.getTtsPath());
        return ResponseEntity.ok(Map.of("message", "에피소드의 TTS가 등록되었습니다."));
    }
}
