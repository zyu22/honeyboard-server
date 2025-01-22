package com.honeyboard.api.auth.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerification {
    String email;
    String code;

    public EmailVerification(String email) {
        this.email = email;
    }
}
