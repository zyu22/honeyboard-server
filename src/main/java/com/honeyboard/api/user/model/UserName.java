package com.honeyboard.api.user.model;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserName {
	
	private int userId;
	private String name;

}
