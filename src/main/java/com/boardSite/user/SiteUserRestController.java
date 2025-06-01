package com.boardSite.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class SiteUserRestController {
	private final SiteUserService siteUserService;

	@PostMapping("/join")
	public ResponseEntity createNewUser(@RequestBody Map<String, Object> param) throws Exception {
		log.info("Join request: {}", param);
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = siteUserService.createNewUser(param);
			return ResponseEntity.status(HttpStatus.CREATED).body(result);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 내부 오류");
		}
	}

	@PostMapping("/login")
	public ResponseEntity loginByUserInfo(@RequestBody Map<String, Object> param, HttpSession session)
			throws Exception {
		log.info("Login request: {}", param);
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = siteUserService.loginByUserInfo(param);
			log.info("로그인 요청 성공");

			Map<String, Object> userInfo = (Map<String, Object>) result.get("userInfo");
			session.setAttribute("loginUser", userInfo.get("username"));
			log.info("loginUser(username): {}", userInfo.get("username"));

			return ResponseEntity.status(HttpStatus.CREATED).body(result);
		} catch (Exception e) {
			log.info("로그인 중 오류 발생");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 내부 오류");
		}

	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpSession session) {
		session.invalidate();
		log.info("로그아웃 성공");
		return ResponseEntity.ok("Logged out successfully");
	}

}
