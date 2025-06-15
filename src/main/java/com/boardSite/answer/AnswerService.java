package com.boardSite.answer;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boardSite.mapper.AnswerMapper;
import com.boardSite.mapper.QuestionMapper;
import com.boardSite.mapper.SiteUserMapper;
import com.boardSite.question.QuestionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnswerService {
	private final AnswerMapper answerMapper;
	private final QuestionMapper questionMapper;

	// 댓글 작성
	@Transactional(readOnly = false)
	public Map<String, Object> createAnswer(Map<String, Object> param) throws Exception {
	    log.info("createAnswer 메서드: {}", param);

	    Map<String, Object> resultMap = new HashMap<>();
	    Map<String, Object> paramMap = new HashMap<>();

	    // 입력 검증
	    Long questionId = param.get("questionId") != null ? Long.valueOf(param.get("questionId").toString()) : null;
	    String content = (String) param.get("content");
	    String username = (String) param.get("username");

	    if (questionId == null || content == null || content.trim().isEmpty() || username == null) {
	        resultMap.put("success", false);
	        resultMap.put("message", "필수 입력 값이 누락되었습니다.");
	        log.info("입력 값 누락");
	        return resultMap;
	    }

	    // 게시글 존재 여부 확인
	    paramMap.put("questionId", questionId);
	    Map<String, Object> questionDetail = questionMapper.questionDetail(paramMap);
	    if (questionDetail == null) {
	        resultMap.put("success", false);
	        resultMap.put("message", "게시글을 찾을 수 없습니다.");
	        log.info("게시글 존재하지 않음: questionId={}", questionId);
	        return resultMap;
	    }

	    // 댓글 저장
	    paramMap.clear();
	    paramMap.put("content", content);
	    paramMap.put("createDate", LocalDateTime.now());
	    paramMap.put("modifyDate", LocalDateTime.now());
	    paramMap.put("questionId", questionId); // 핵심: questionId 정확히 설정
	    paramMap.put("author", username);

	    int insertResult = answerMapper.insertAnswer(paramMap);

	    if (insertResult != 1 || paramMap.get("answerId") == null) {
	        resultMap.put("success", false);
	        resultMap.put("message", "댓글 작성에 실패했습니다.");
	        log.info("댓글 작성 실패");
	        return resultMap;
	    }

	    // Mybatis에서 key값으로 answerId 자동으로 받아옴.
	    Map<String, Object> answer = new HashMap<>();
	    answer.put("answerId", paramMap.get("answerId"));
	    answer.put("questionId", questionId);
	    answer.put("author", username);
	    answer.put("content", content);
	    answer.put("createDate", paramMap.get("createDate"));
	    answer.put("modifyDate", paramMap.get("modifyDate"));

	    resultMap.put("success", true);
	    resultMap.put("message", "댓글이 성공적으로 등록되었습니다.");
	    resultMap.put("answer", answer);
	    return resultMap;
	}

	// 댓글 수정
	@Transactional(readOnly = false)
	public Map<String, Object> updateAnswer(Map<String, Object> param) throws Exception {
		log.info("updateAnswer 메서드: {}", param);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<>();

		// TODO 게시글 존재 여부 확인
		paramMap.put("answerId", param.get("answerId"));
		Map<String, Object> answerDetail = answerMapper.answerDetail(paramMap);
		if (answerDetail == null) {
			resultMap.put("success", false);
			resultMap.put("message", "댓글을 찾을 수 없습니다.");
			log.info("댓글 존재하지 않음");
			return resultMap;
		}

		// TODO 권한 체크
		String dbAuthor = (String) answerDetail.get("author");
		String username = (String) param.get("username");
		if (username == null) {
			resultMap.put("success", false);
			resultMap.put("message", "댓글 수정 권한이 없습니다.");
			log.info("댓글 수정 권한 없음");
			return resultMap;
		}

		// TODO 댓글 수정
		paramMap.put("content", param.get("content"));
		paramMap.put("updateDate", LocalDateTime.now());

		int updateResult = answerMapper.updateAnswer(paramMap);
		if (updateResult != 1) {
			resultMap.put("success", false);
			resultMap.put("message", "댓글 수정에 실패했습니다.");
			log.info("댓글 수정 실패");
			return resultMap;
		}

		resultMap.put("success", true);
		resultMap.put("message", "댓글이 성공적으로 수정되었습니다.");
		log.info("댓글 수정 성공");
		return resultMap;
	}

	// 댓글 수정
	@Transactional(readOnly = false)
	public Map<String, Object> deleteAnswer(Map<String, Object> param) throws Exception {
		log.info("deleteAnswer 메서드: {}", param);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<>();

		// TODO 게시글 존재 여부 확인
		paramMap.put("answerId", param.get("answerId"));
		Map<String, Object> answerDetail = answerMapper.answerDetail(paramMap);
		if (answerDetail == null) {
			resultMap.put("success", false);
			resultMap.put("message", "댓글을 찾을 수 없습니다.");
			log.info("댓글 존재하지 않음");
			return resultMap;
		}

		// TODO 권한 체크
		String dbAuthor = (String) answerDetail.get("author");
		String username = (String) param.get("username");
		if (username == null) {
			resultMap.put("success", false);
			resultMap.put("message", "댓글 삭제 권한이 없습니다.");
			log.info("댓글 삭제 권한 없음");
			return resultMap;
		}

		// TODO 댓글 수정
		paramMap.put("answerId", param.get("answerId"));

		int deleteResult = answerMapper.deleteAnswer(paramMap);
		if (deleteResult != 1) {
			resultMap.put("success", false);
			resultMap.put("message", "댓글 삭제에 실패했습니다.");
			log.info("댓글 삭제 실패");
			return resultMap;
		}

		resultMap.put("success", true);
		resultMap.put("message", "댓글이 성공적으로 삭제되었습니다.");
		log.info("댓글 삭제 성공");
		return resultMap;
	}

	// 댓글 상세 조회
	@Transactional(readOnly = false)
	public Map<String, Object> getAnswerDetail(Map<String, Object> param) throws Exception {
		log.info("getAnswerDetail 메서드: {}", param);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<>();

		// TODO 댓글 존재 여부 확인
		paramMap.put("answerId", param.get("answerId"));
		Map<String, Object> answerDetail = answerMapper.answerDetail(paramMap);
		if (answerDetail == null) {
			resultMap.put("success", false);
			resultMap.put("message", "댓글을 찾을 수 없습니다.");
			log.info("댓글 존재하지 않음");
			return resultMap;
		}

		resultMap.put("answerDetail", answerDetail);
		resultMap.put("success", true);
		resultMap.put("message", "댓글이 성공적으로 조회했습니다..");
		log.info("댓글 조회 성공");
		return resultMap;
	}

	// 게시글의 댓글 목록
	@Transactional(readOnly = true)
	public List<Map<String, Object>> getAnswerList(Map<String, Object> param) {
		log.info("getAnswerList 메서드: {}", param);
		return answerMapper.selectAnswerList(param);
	}
}
