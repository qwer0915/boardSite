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
			String username = (String) param.get("author");
			if (username == null) {
			    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("작성자 정보가 없습니다.");
			}

			Map<String, Object> answerParam = new HashMap<>();
			answerParam.put("content", param.get("content"));
			answerParam.put("questionId", questionId);
			answerParam.put("username", username);

			Map<String, Object> result = answerService.createAnswer(answerParam);
			
			log.info("result {result}");
			return ResponseEntity.status(HttpStatus.CREATED).body(result);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("서버 내부 오류: " + e.getMessage());
		}
	}
	
	
	@PostMapping("/update/{answerId}")
	public ResponseEntity<?> updateAnswer(@PathVariable("answerId") Integer answerId,
			@RequestBody Map<String, Object> param, HttpSession session) {
		try {
			String username = (String) param.get("author");
			if (username == null) {
			    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("작성자 정보가 없습니다.");
			}

			Map<String, Object> answerParam = new HashMap<>();
			answerParam.put("content", param.get("content"));
			answerParam.put("answerId", answerId);
			answerParam.put("author", username); // username으로 저장

			Map<String, Object> result = answerService.updateAnswer(answerParam);
			return ResponseEntity.status(HttpStatus.CREATED).body(result);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("서버 내부 오류: " + e.getMessage());
		}
	}
	
	
	@PostMapping("/delete/{answerId}")
	public ResponseEntity<?> deleteAnswer(@PathVariable("answerId") int id, @RequestBody Map<String, Object> param,
			HttpSession session) {
		log.info("delete question request: {}", param);
		try {
			// 1. 로그인 체크
			String username = (String) param.get("author");
			if (username == null) {
			    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("작성자 정보가 없습니다.");
			}

			// 2. 파라미터에 id, author 추가
			param.put("id", id);
			param.put("username", username);
			// 3. 서비스 호출
			Map<String, Object> result = answerService.deleteAnswer(param);

			// 4. 결과 반환
			if (Boolean.TRUE.equals(result.get("success"))) {
				return ResponseEntity.ok(result);
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result.get("message"));
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 내부 오류");
		}
	}

}
