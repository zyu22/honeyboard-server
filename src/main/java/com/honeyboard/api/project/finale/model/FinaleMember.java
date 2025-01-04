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

	private Integer finaleMemberId;
	private Integer finaleTeamId;
	private Integer userId;
	private String name;
	private String role;
	private LocalDate createdAt;

}
