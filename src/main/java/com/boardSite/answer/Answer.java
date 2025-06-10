package com.boardSite.answer;

import java.time.LocalDateTime;

import com.boardSite.question.Question;
import com.boardSite.user.SiteUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Answer {
	private Integer id;
	private String content;
	private LocalDateTime createDate;
	private String author;
	private LocalDateTime modifyDate;
}
