package com.honeyboard.api.project.finale.model;

import java.time.LocalDate;

import com.honeyboard.api.user.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FinaleMember {

	private int finaleMemberId;
	private int finaleTeamId;
	private User user;

	private enum role {
		LEADER, MEMBER;
	};

	private LocalDate createdAt;

}
