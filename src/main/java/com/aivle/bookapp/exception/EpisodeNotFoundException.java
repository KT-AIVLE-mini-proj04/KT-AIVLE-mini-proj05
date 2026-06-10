package com.aivle.bookapp.exception;

public class EpisodeNotFoundException extends RuntimeException {
    public EpisodeNotFoundException(Long id) {
        super("Episode with ID " + id + " not found.");
    }
}

