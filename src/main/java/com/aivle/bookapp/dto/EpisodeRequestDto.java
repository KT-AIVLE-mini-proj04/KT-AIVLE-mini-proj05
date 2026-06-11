package com.aivle.bookapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EpisodeRequestDto {

    @NotNull(message = "책 ID는 필수입니다.")
    private Long bookId;

    @NotNull(message = "사용자 ID는 필수입니다.")
    private Integer usersId;

    @NotBlank(message = "에피소드 제목은 필수입니다.")
    private String episodeTitle;

    @NotNull(message = "에피소드 순서는 필수입니다.")
    private Integer episodeIndex;

    @NotBlank(message = "에피소드 내용은 필수입니다.")
    private String content;
}