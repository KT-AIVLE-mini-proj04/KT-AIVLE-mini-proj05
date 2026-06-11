package com.aivle.bookapp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EpisodeUpdateRequest {

    private String episodeTitle;
    private Integer episodeIndex;
    private String content;
}