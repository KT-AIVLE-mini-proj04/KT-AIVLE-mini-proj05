package com.aivle.bookapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long episodeId;

    @NotNull
    @Column(nullable = false)
    private Long bookId;

    @NotNull
    @Column(nullable = false)
    private Long usersId;

    @Column(nullable = false)
    @NotBlank
    private String episodeTitle;

    @Column
    private Integer view = 0;

    @Column(nullable = false)
    private Integer episodeIndex;

    @Column(nullable = false)
    @NotBlank
    private String content;

    @Column
    private String ttsPath;

    @CreationTimestamp
    @Column
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;
}
