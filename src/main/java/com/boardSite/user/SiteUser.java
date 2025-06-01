package com.boardSite.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor // 추가!
@ToString
public class SiteUser {
	private Long id;
	private String username;
	private String password;
	private String email;
	
	public SiteUser(String username, String password, String email) {
		this.username=username;
		this.password=password;
		this.email=email;
	}
}
