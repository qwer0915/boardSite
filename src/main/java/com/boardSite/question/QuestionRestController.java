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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.boardSite.answer.AnswerService;
import com.boardSite.user.SiteUserService;

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
	public ResponseEntity createQuestion(@RequestBody Map<String, Object> param,
			HttpSession session
	) throws Exception {
		log.info("insert question request: {}", param);
		try {
			String username = (String) session.getAttribute("loginUser");
			if (username == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
			}
			param.put("author", username);
			Map<String, Object> result = questionService.createQuestion(param);
			return ResponseEntity.status(HttpStatus.CREATED).body(result);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 내부 오류");
		}
	}

	// 글 수정
	@PostMapping("/update/{questionId}")
	public ResponseEntity<?> updateQuestion(@PathVariable("questionId") int questionId, @RequestBody Map<String, Object> param,
			HttpSession session) {
		log.info("update question request: {}", param);
		try {
			String username = (String) session.getAttribute("loginUser");
			if (username == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
			}

			// 2. 파라미터에 id, author 추가
			param.put("questionId", questionId);
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
	@PostMapping("/delete/{questionId}")
	public ResponseEntity<?> deleteQuestion(@PathVariable("questionId") int questionId, @RequestBody Map<String, Object> param,
			HttpSession session) {
		log.info("delete question request: {}", param);
		try {
			// 1. 로그인 체크
			String username = (String) session.getAttribute("loginUser");
			if (username == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
			}

			// 2. 파라미터에 id, author 추가
			param.put("questionId", questionId);
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
	@GetMapping("/{questionId}")
	public ResponseEntity getQuestionDetail(@PathVariable("questionId") int questionId) {
		Map<String, Object> param = new HashMap<>();
		param.put("questionId", questionId);
		Map<String, Object> question = questionService.getQuestionDetail(param);

		Map<String, Object> answerParam = new HashMap<>();
		answerParam.put("questionId", questionId);
		List<Map<String, Object>> answerList = answerService.getAnswerList(answerParam);

		log.info("answerList: {}", answerList);
		log.info("question: {}", question);
		if (question == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시글을 찾을 수 없습니다.");
		}
		return ResponseEntity.ok(question);
	}

	// 글 검색
	@RequestMapping(value = "/list", method = {RequestMethod.GET,RequestMethod.POST})
	public ResponseEntity getQuestionListBySearch(
			@RequestParam(value = "searchSubjectName", required = false) String searchSubjectName,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum) {
		Map<String, Object> param = new HashMap<>();
		param.put("searchSubjectName", searchSubjectName);
		param.put("pageNum", pageNum);
		Map<String, Object> result = questionService.getQuestionList(param);
		return ResponseEntity.ok(result);
	}

}
