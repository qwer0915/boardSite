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

	// 글 작성
	@PostMapping("/insert")
	public ResponseEntity createQuestion(@RequestBody Map<String, Object> param, HttpSession session) throws Exception {
		log.info("insert question request: {}", param);
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String username = (String) session.getAttribute("loginUser");
			param.put("author", username);
			log.info("author: {}", username);
			result = questionService.createQuestion(param);
			log.info("question detail: {}", param);
			return ResponseEntity.status(HttpStatus.CREATED).body(result);
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
		log.info("question: {}", question);
		if (question == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시글을 찾을 수 없습니다.");
		}
		return ResponseEntity.ok(question);
	}
}
