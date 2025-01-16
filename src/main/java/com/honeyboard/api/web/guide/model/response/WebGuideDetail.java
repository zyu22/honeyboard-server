package com.honeyboard.api.web.guide.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WebGuideDetail {
	private int id;
	private String title;
	private String content;
	private int authorId;
	private String authorName;
	private boolean bookmarked;
	private String createdAt;
}
