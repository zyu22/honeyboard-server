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
	private String title;
	private Date startDate;
	private Date endDate;
	private String scheduleType;
	private boolean is_public;
	private int userId;
	private int generationId;
	private Date createdAt;
	private Date updatedAt;
	
}
