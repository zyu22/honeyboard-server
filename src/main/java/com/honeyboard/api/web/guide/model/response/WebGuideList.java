package com.honeyboard.api.web.guide.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WebGuideList {
	private int id;
	private String title;
	private String thumbnail;
	private String createdAt;
}
