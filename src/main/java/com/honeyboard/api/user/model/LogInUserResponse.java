package com.honeyboard.api.user.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public
class LogInUserResponse {

    private int userId = 0;
    private String name;
    private int generationId = 0;
    private int generationName = 0;
    private String role;

}
