package com.aivle.bookapp.dto;

public record BookLikeResponse(
        boolean isLiked,
        int totalLikes
) {
    public static BookLikeResponse of(boolean isLiked, int totalLikes) {
        return new BookLikeResponse(isLiked, totalLikes);
    }
}