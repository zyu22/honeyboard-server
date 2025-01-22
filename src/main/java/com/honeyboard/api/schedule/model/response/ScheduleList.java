package com.honeyboard.api.schedule.model.response;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleList {

    private int id;
    private String content;
    private String startDate;
    private String endDate;
    private String scheduleType;
}
