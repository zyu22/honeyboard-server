package com.honeyboard.api.user.model;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

	private long userId;
	private String email;
	private String password;
	private String name;
	private long generationId;
	private String role;
	private String loginType;
	private boolean isSsafy;
	private Date createdAt;
	
}
