package com.aivle.bookapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CommentUpdateRequest {

    @NotBlank(message = "댓글 내용은 필수입니다.")
    @Size(min = 1, max = 1000, message = "댓글은 1~1000자 이내로 입력해주세요.")
    private String content;

    protected CommentUpdateRequest() {}

    public String getContent() { return content; }
}
