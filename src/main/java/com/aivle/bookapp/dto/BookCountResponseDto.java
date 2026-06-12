package com.aivle.bookapp.dto;

public class BookCountResponseDto {
    private final long totalBookCount;
    private final long coverBookCount;
    private final long likedBookCount;

    public BookCountResponseDto(long totalBookCount, long coverBookCount, long likedBookCount) {
        this.totalBookCount = totalBookCount;
        this.coverBookCount = coverBookCount;
        this.likedBookCount = likedBookCount;
    }

    public long getTotalBookCount() {
        return totalBookCount;
    }

    public long getCoverBookCount() {
        return coverBookCount;
    }

    public long getLikedBookCount() {
        return likedBookCount;
    }
}
