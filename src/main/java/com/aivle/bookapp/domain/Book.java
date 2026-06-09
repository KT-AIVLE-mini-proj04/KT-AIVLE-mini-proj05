package com.aivle.bookapp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

//    private int likeCount = 0;
//
//    public void increaseLikeCount() {
//        this.likeCount++;
//    }
//
//    public void decreaseLikeCount() {
//        if (this.likeCount > 0) {
//            this.likeCount--;
//        }
//    }
}
