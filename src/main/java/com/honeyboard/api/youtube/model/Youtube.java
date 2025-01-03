package com.honeyboard.api.youtube.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Youtube {

    private int id;
    private String videoId;
    private String title;
    private String channel;
    private int generationId;
    private LocalDate createdAt;
}
