package com.aivle.bookapp.dto;

import com.aivle.bookapp.domain.Episode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EpisodeResponseDto {

    private final Long episodeId;
    private final Long bookId;
    private final Integer usersId;
    private final String episodeTitle;
    private final Integer view;
    private final Integer episodeIndex;
    private final String content;
    private final String ttsPath;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public EpisodeResponseDto(Episode episode) {
        this.episodeId = episode.getEpisodeId();
        this.bookId = episode.getBookId();
        this.usersId = episode.getUsersId();
        this.episodeTitle = episode.getEpisodeTitle();
        this.view = episode.getView();
        this.episodeIndex = episode.getEpisodeIndex();
        this.content = episode.getContent();
        this.ttsPath = episode.getTtsPath();
        this.createdAt = episode.getCreatedAt();
        this.updatedAt = episode.getUpdatedAt();
    }
}
