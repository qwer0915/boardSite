package com.boardSite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.boardSite.answer.AnswerService;
import com.boardSite.mapper.QuestionMapper;
import com.boardSite.question.QuestionService;
import com.boardSite.user.SiteUser;
import com.boardSite.user.SiteUserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;

@SpringBootTest
@AutoConfigureMockMvc
class BoardSiteApplicationTests {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private QuestionService questionService;

	@Autowired
	private SiteUserService siteUserService;

//	@Test
//	void 회원가입() throws Exception{
//		Map<String, Object> param = new HashMap<>();
//		param.put("username", "111");
//		param.put("password", "111");
//		param.put("email", "111");
//		
//		Map<String, Object> result = siteUserService.createNewUser(param);
//		System.out.println("result: "+result);
//	}
	
//	@Test
//	void 로그인() throws Exception{
//		Map<String, Object> param = new HashMap<>();
//		param.put("username", "111");
//		param.put("password", "111");
//		
//		Map<String, Object> result = siteUserService.loginByUserInfo(param);
//		System.out.println("result: "+result);
//		
//		
//	}
//	
	
	
//	@Test
//	void 로그인_후_글_작성_서비스직접호출() throws Exception {
//	    // 1. 회원 가입 (필요하다면)
//	    Map<String, Object> joinParam = new HashMap<>();
//	    joinParam.put("username", "testuser");
//	    joinParam.put("password", "testpass");
//	    joinParam.put("email", "testuser@example.com");
//	    siteUserService.createNewUser(joinParam);

//	    // 2. 로그인
//	    Map<String, Object> loginParam = new HashMap<>();
//	    loginParam.put("username", "111");
//	    loginParam.put("password", "111");
//	    Map<String, Object> loginResult = siteUserService.loginByUserInfo(loginParam);
//	    System.out.println("로그인 결과: " + loginResult);
//
//	    // 3. 로그인 성공 시 글 작성
//	    Map<String, Object> userInfo = (Map<String, Object>) loginResult.get("userInfo");
//	    String username = (String) userInfo.get("username");
//
//	    Map<String, Object> questionParam = new HashMap<>();
//	    questionParam.put("subject", "서비스 직접 호출 테스트 제목2");
//	    questionParam.put("content", "서비스 직접 호출 테스트 내용2");
//	    questionParam.put("author", username);
//
//	    Map<String, Object> createResult = questionService.createQuestion(questionParam);
//	    System.out.println("글 작성 결과: " + createResult);
//
//	    // 4. 결과 검증 (Optional)
//	    assertTrue((Boolean) createResult.get("success"));
//	}
	
//	@Test
//	void 질문_상세_조회시_답변_리스트_포함() throws Exception {
//		int questionId = 2;
//		Map<String, Object> param = new HashMap<>();
//		param.put("questionId", questionId);
//		Map<String, Object> question = questionService.getQuestionDetail(param);
//		System.out.println("question: "+question);
////		mockMvc.perform(get("/question/{questionId}", questionId).contentType(MediaType.APPLICATION_JSON))
////				.andExpect(status().isOk())
////				// 답변 리스트가 포함되어 있는지 확인
////				.andExpect(jsonPath("$.answers").isArray());
//	}

//	@Test
//	void 글_목록() {
//		Map<String, Object> param = new HashMap<>();
//		Map<String, Object> result = questionService.getQuestionList(param);
//		System.out.println("result: " + result);
//	}

//	@Test
//	void 글_목록() {
//		Map<String, Object> param = new HashMap<>();
//		param.put("searchSubjectName", "2");
//		param.put("pageNum", 1);
//		Map<String, Object> result = questionService.getQuestionList(param);
//		System.out.println("result: " + result);
//	}


	@Test
	void 로그인_후_글_수정_서비스직접호출() throws Exception {
	    // 1. 로그인
		
	    Map<String, Object> loginParam = new HashMap<>();
	    loginParam.put("username", "111");
	    loginParam.put("password", "111");
	    Map<String, Object> loginResult = siteUserService.loginByUserInfo(loginParam);
	    System.out.println("로그인 결과: " + loginResult);

	    // 3. 로그인 성공 시 글 작성
	    Map<String, Object> userInfo = (Map<String, Object>) loginResult.get("userInfo");
	    String username = (String) userInfo.get("username");

	    Map<String, Object> questionParam = new HashMap<>();
	    int questionId = 2;
	    questionParam.put("questionId", questionId);
	    questionParam.put("author", username);
	    questionParam.put("subject", "서비스 직접 호출 테스트 제목2 수정 버전");
	    questionParam.put("content", "서비스 직접 호출 테스트 내용2 수정 버전");

	    Map<String, Object> createResult = questionService.updateQuestion(questionParam);
	    System.out.println("글 수정 결과: " + createResult);

	    // 4. 결과 검증 (Optional)
	    assertTrue((Boolean) createResult.get("success"));
	}
	
	

}
