package com.boardSite.answer;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boardSite.question.QuestionService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/answer")
public class AnswerRestController {
	private final AnswerService answerService;

	@PostMapping("/insert/{questionId}")
	public ResponseEntity<?> createAnswer(@PathVariable("questionId") Integer questionId,
			@RequestBody Map<String, Object> param, HttpSession session) {
		try {
			String username = (String) session.getAttribute("loginUser");
			if (username == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
			}

			Map<String, Object> answerParam = new HashMap<>();
			answerParam.put("content", param.get("content"));
			answerParam.put("questionId", questionId);
			answerParam.put("author", username); // username으로 저장

			Map<String, Object> result = answerService.createAnswer(answerParam);
			return ResponseEntity.status(HttpStatus.CREATED).body(result);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("서버 내부 오류: " + e.getMessage());
		}
	}
	
	
	@PostMapping("/update/{questionId}")
	public ResponseEntity<?> updateAnswer(@PathVariable("questionId") Integer questionId,
			@RequestBody Map<String, Object> param, HttpSession session) {
		try {
			String username = (String) session.getAttribute("loginUser");
			if (username == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
			}

			Map<String, Object> answerParam = new HashMap<>();
			answerParam.put("content", param.get("content"));
			answerParam.put("questionId", questionId);
			answerParam.put("author", username); // username으로 저장

			Map<String, Object> result = answerService.updateAnswer(answerParam);
			return ResponseEntity.status(HttpStatus.CREATED).body(result);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("서버 내부 오류: " + e.getMessage());
		}
	}

}
