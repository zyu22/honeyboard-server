package com.honeyboard.api.project.finale.model;

import java.util.List;

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
public class FinaleTeam {

	private int teamId;
	private List<FinaleMember> members;
	private List<FinaleMember> nonParticipants;
	private boolean hasSubmitted;

}
