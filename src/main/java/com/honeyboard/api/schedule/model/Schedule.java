package com.honeyboard.api.schedule.model;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class Schedule {

	private int scheduleId;
	private String content;
	private Date startDate;
	private Date endDate;
	private String scheduleType;
	private boolean publicAccess;
	private int userId;
	private int generationId;

}
