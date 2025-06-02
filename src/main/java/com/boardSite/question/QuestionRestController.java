package com.boardSite.question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.boardSite.answer.AnswerService;
import com.boardSite.user.SiteUserService;

import ch.qos.logback.core.recovery.ResilientSyslogOutputStream;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/question")
public class QuestionRestController {
	private final QuestionService questionService;
	private final AnswerService answerService;

	// 글 작성
	@PostMapping("/create")
	public ResponseEntity createQuestion(@RequestBody Map<String, Object> param, HttpSession session) throws Exception {
		log.info("insert question request: {}", param);
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String username = (String) session.getAttribute("loginUser");
			if (username == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
			}
			param.put("author", username);
			log.info("author: {}", username);
			result = questionService.createQuestion(param);
			log.info("question detail: {}", param);
			return ResponseEntity.status(HttpStatus.CREATED).body(result);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 내부 오류");
		}
	}

	// 글 수정
	@PostMapping("/update/{id}")
	public ResponseEntity<?> updateQuestion(@PathVariable("id") int id, @RequestBody Map<String, Object> param,
			HttpSession session) {
		try {
			// 1. 로그인 체크
			String username = (String) session.getAttribute("loginUser");
			if (username == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
			}

			// 2. 파라미터에 id, author 추가
			param.put("id", id);
			param.put("username", username);
			// 3. 서비스 호출
			Map<String, Object> result = questionService.updateQuestion(param);

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

	// 글 삭제
	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteQuestion(@PathVariable("id") int id, @RequestBody Map<String, Object> param,
			HttpSession session) {
		try {
			// 1. 로그인 체크
			String username = (String) session.getAttribute("loginUser");
			if (username == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
			}

			// 2. 파라미터에 id, author 추가
			param.put("id", id);
			param.put("username", username);
			// 3. 서비스 호출
			Map<String, Object> result = questionService.deleteQuestion(param);

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

	// 글 상세 조회
	@GetMapping("/{id}")
	public ResponseEntity getQuestionDetail(@PathVariable("id") int id) {
		Map<String, Object> param = new HashMap<>();
		param.put("id", id);
		Map<String, Object> question = questionService.getQuestionDetail(param);
		
		Map<String, Object> answerParam = new HashMap<>();
		answerParam.put("question_id", id);
		List<Map<String, Object>> answerList = answerService.getAnswerList(answerParam);
		
		log.info("answerList: {}", answerList);
		log.info("question: {}", question);
		if (question == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시글을 찾을 수 없습니다.");
		}
		return ResponseEntity.ok(question);
	}

}
