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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import com.boardSite.answer.AnswerService;
import com.boardSite.mapper.QuestionMapper;
import com.boardSite.question.QuestionService;
import com.boardSite.user.SiteUser;

@SpringBootTest
@AutoConfigureMockMvc
class BoardSiteApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Test
	void 질문_상세_조회시_답변_리스트_포함() throws Exception {
		int questionId = 1;
		mockMvc.perform(get("/question/{id}", questionId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				// 답변 리스트가 포함되어 있는지 확인
				.andExpect(jsonPath("$.answers").isArray());
	}
}
