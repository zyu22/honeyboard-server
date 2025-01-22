package com.honeyboard.api.schedule.model.request;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SceduleRequest {

    private String content;
    private String startDate;
    private String endDate;
    private String scheduleType;
    private boolean publicAccess;
}
