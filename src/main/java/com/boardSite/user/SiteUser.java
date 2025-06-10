package com.boardSite.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class SiteUser {
	private Long id;
	private String username;
	private String password;
	private String email;
	
}
