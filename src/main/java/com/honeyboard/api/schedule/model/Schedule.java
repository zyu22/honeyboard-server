package com.honeyboard.api.schedule.model;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class Schedule {

	private int scheduleId;
	private String content;
	private LocalDate startDate;
	private LocalDate endDate;
	private String scheduleType;
	private boolean isPublic;
	private int userId;
	private int generationId;

}
