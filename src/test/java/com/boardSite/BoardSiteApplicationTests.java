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
//	void 질문_상세_조회시_답변_리스트_포함() throws Exception {
//		int questionId = 1;
//		mockMvc.perform(get("/question/{id}", questionId).contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				// 답변 리스트가 포함되어 있는지 확인
//				.andExpect(jsonPath("$.answers").isArray());
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
//		param.put("searchSubjectName", "수정");
//		param.put("pageNum", 1);
//		Map<String, Object> result = questionService.getQuestionList(param);
//		System.out.println("result: " + result);
//	}

//@Test
	void 로그인_후_글_작성() throws Exception {
	    // 1. 사용자 회원가입
	    Map<String, String> joinParams = new HashMap<>();
	    joinParams.put("username", "testUser");
	    joinParams.put("password", "testPass");
	    mockMvc.perform(post("/user/join")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(new ObjectMapper().writeValueAsString(joinParams)))
	            .andExpect(status().isCreated());

	    // 2. 로그인 및 세션 획득
	    MvcResult loginResult = mockMvc.perform(post("/user/login")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(new ObjectMapper().writeValueAsString(joinParams)))
	            .andExpect(status().isCreated())
	            .andReturn();

	    HttpSession session = loginResult.getRequest().getSession();

	    // 3. 글 작성 요청 (세션 포함)
	    Map<String, String> questionParams = new HashMap<>();
	    questionParams.put("subject", "테스트 제목2");
	    questionParams.put("content", "테스트 내용2");

	    mockMvc.perform(post("/question/create")
	            .session((MockHttpSession) session)  // 로그인 세션 전달
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(new ObjectMapper().writeValueAsString(questionParams)))
	            .andExpect(status().isCreated())
	            .andExpect(jsonPath("$.success").value(true))
	            .andExpect(jsonPath("$.message").value("글 작성 성공"));
	}

	
	

}
