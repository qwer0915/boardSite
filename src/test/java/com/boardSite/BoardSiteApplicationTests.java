package com.boardSite;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.http.HttpHeaders;
import java.util.HashMap;
import java.util.Map;

import com.boardSite.question.QuestionService;
import com.boardSite.user.SiteUser;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BoardSiteApplicationTests {
	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private QuestionService questionService;

//	@Test
//	void 회원가입_성공_테스트() {
////		SiteUser user = new SiteUser("test1", "test1","test1");
//		Map<String, Object> param = new HashMap<>();
//		param.put("username", "testuser1");
//		param.put("password", "testpass1");
//		param.put("email", "test1@example.com");
//		ResponseEntity<Void> response = restTemplate.postForEntity("/user/join", param, Void.class);
//
//		assertEquals(HttpStatus.OK, response.getStatusCode(), "회원가입 요청의 응답 코드가 200 OK가 아닙니다.");
//	}

//	@Test
//	void 로그인_성공_테스트() {
//		// given: 이미 DB에 존재하는 username/password 사용
//		Map<String, Object> param = new HashMap<>();
//		param.put("username", "test1");
//		param.put("password", "test1");
//
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
//		HttpEntity<Map<String, Object>> request = new HttpEntity<>(param, headers);
//
//		// when
//		ResponseEntity<Map> response = restTemplate.postForEntity("/user/login", request, Map.class);
//
//		// then
//		assertEquals(HttpStatus.OK, response.getStatusCode());
//		assertNotNull(response.getBody());
//		assertEquals("testuser", response.getBody().get("username"));
//		assertFalse(response.getBody().containsKey("password")); // password는 반환하지 않음
//	}
//
//	@Test
//	void 로그인_후_질문_작성_성공() {
//		// 1. 로그인
//		HttpHeaders loginHeaders = new HttpHeaders();
//		loginHeaders.setContentType(MediaType.APPLICATION_JSON);
//		String loginJson = """
//				{"username":"test1","password":"test1"}
//				""";
//		ResponseEntity<Map> loginResponse = restTemplate.postForEntity("/user/login",
//				new HttpEntity<>(loginJson, loginHeaders), Map.class);
//		String sessionCookie = loginResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
//
//		// 2. 질문 작성
//		HttpHeaders questionHeaders = new HttpHeaders();
//		questionHeaders.setContentType(MediaType.APPLICATION_JSON);
//		questionHeaders.add(HttpHeaders.COOKIE, sessionCookie);
//		String questionJson = """
//				{"subject":"제목","content":"내용"}
//				""";
//		ResponseEntity<Void> response = restTemplate.postForEntity("/question/insert",
//				new HttpEntity<>(questionJson, questionHeaders), Void.class);
//
//		// 3. 검증
//		assertEquals(HttpStatus.OK, response.getStatusCode());
//	}

	@Test
	void 글_데이터_조회() {
		int id = 1;
		Map<String, Object> param = new HashMap<>();
		param.put("id", id);

		// when
		Map<String, Object> result = questionService.getQuestionDetail(param);

		// when

		// then
		assertNotNull(result, "결과 Map이 null이 아니어야 합니다.");
		Map<String, Object> questionDetail = (Map<String, Object>) result.get("questionDetail");
		assertNotNull(questionDetail, "questionDetail이 null이 아니어야 합니다.");
		assertEquals(id, questionDetail.get("id"));
		String content = (String) questionDetail.get("content");
		System.out.println("Content: " + content);
	}
}
