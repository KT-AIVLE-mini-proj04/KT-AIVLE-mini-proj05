package com.aivle.bookapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TtsUpdateRequest {

    @NotBlank(message = "TTS 경로는 필수입니다.")
    private String ttsPath;
}