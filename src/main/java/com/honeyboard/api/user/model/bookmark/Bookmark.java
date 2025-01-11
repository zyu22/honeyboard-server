package com.honeyboard.api.user.model.bookmark;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Bookmark {
        public int userId;
        public String contentType;
        public int contentId;
        public LocalDate createdAt;
}
