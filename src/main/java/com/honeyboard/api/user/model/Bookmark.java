package com.honeyboard.api.user.model;
import lombok.*;

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
        public Date createdAt;

}
