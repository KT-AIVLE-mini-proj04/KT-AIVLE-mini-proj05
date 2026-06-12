package com.aivle.bookapp.exception;

public class EpisodeNotFoundException extends RuntimeException {
    public EpisodeNotFoundException(Long id) {
        super("ID " + id + "에 해당하는 에피소드를 찾을 수 없습니다.");
    }
}
