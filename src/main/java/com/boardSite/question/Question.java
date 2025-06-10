package com.boardSite.question;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Question {
	private Integer id;
	private String subject;
	private String content;
	private LocalDateTime createDate;
	private String author;
	private LocalDateTime modifyDate;
}
