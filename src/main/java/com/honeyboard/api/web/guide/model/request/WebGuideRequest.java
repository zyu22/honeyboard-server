package com.honeyboard.api.web.guide.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WebGuideRequest {
	private int id;
	private String title;
	private String content;
	private String thumbnail;
}
