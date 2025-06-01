package com.boardSite.question;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boardSite.mapper.QuestionMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService {
	private final QuestionMapper questionMapper;

	// TODO 모든 데이터는 map으로 받는다, mapper에는 paramMap이라는 map을 던져 데이터 전송,
	// 결과는 resultMap에 담아서 던진다.

//	@Transactional
//	public void createQuestion(Question question, String username) {
//		question.setAuthor(username);
//		question.setCreateDate(LocalDateTime.now());
//		questionMapper.insertQuestion(question);
//	}

	// 글 작성
	@Transactional(readOnly = false)
	public Map<String, Object> createQuestion(Map<String, Object> param) throws Exception {
		log.info("QuestionInfo: {}", param);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<>();

		// TODO 입력값 검증

		// TODO 글 작성
		paramMap.put("subject", param.get("subject"));
		paramMap.put("content", param.get("content"));
		paramMap.put("createDate", LocalDateTime.now());
		paramMap.put("author", param.get("author"));
		int insertQuestionResult = questionMapper.insertQuestion(paramMap);
		
		if(insertQuestionResult != 1) {
			throw new Exception("!!!");
		}
		resultMap.put("success", true);
		resultMap.put("message", "글 작성 성공");
		return resultMap;
	}


	// 글 상세 조회
	@Transactional(readOnly = true)
	public Map<String, Object> getQuestionDetail(Map<String, Object> param){
		log.info("QuestionInfo: {}", param);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<>();
		
		paramMap.put("id", param.get("id"));
		Map<String, Object> question_detail = questionMapper.questionDetail(paramMap);
		resultMap.put("questionDetail", question_detail);
		
		return resultMap;
	}
}
