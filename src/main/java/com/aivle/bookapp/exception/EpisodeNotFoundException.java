package com.aivle.bookapp.exception;

public class EpisodeNotFoundException extends RuntimeException {
    public EpisodeNotFoundException(Long id) {
        super("ID " + id + "에 해당하는 에피소드를 찾을 수 없습니다.");
    }

    public static EpisodeNotFoundException byBookId(Long bookId) {
        return new EpisodeNotFoundException("Book ID " + bookId + "에 해당하는 에피소드가 없습니다.");
    }

    private EpisodeNotFoundException(String message) {
        super(message);
    }
}
