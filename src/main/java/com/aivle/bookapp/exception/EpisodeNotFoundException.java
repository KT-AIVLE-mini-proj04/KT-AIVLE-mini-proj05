package com.aivle.bookapp.exception;

public class EpisodeNotFoundException extends RuntimeException {
    public EpisodeNotFoundException(Long id) {
        super("Episode with ID " + id + " not found.");
    }

    public static EpisodeNotFoundException byBookId(Long bookId) {
        return new EpisodeNotFoundException("Book ID " + bookId + "에 해당하는 에피소드가 없습니다.");
    }

    private EpisodeNotFoundException(String message) {
        super(message);
    }
}
