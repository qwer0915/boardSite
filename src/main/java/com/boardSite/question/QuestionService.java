package com.boardSite.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

	// 글 작성
	@Transactional(readOnly = false)
	public Map<String, Object> createQuestion(Map<String, Object> param) throws Exception {
		log.info("Insert QuestionInfo: {}", param);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<>();

		// TODO 입력값 검증

		// TODO 글 작성
		paramMap.put("subject", param.get("subject"));
		paramMap.put("content", param.get("content"));
		paramMap.put("createDate", LocalDateTime.now());
		paramMap.put("author", param.get("author"));
		int insertQuestionResult = questionMapper.insertQuestion(paramMap);

		if (insertQuestionResult != 1) {
			throw new Exception("!!!");
		}
		resultMap.put("success", true);
		resultMap.put("message", "글 작성 성공");
		return resultMap;
	}

	// 글 수정
	@Transactional(readOnly = false)
	public Map<String, Object> updateQuestion(Map<String, Object> param) throws Exception {
		log.info("Update QuestionInfo: {}", param);
		Map<String, Object> resultMap = new HashMap<>();

		// 1. 게시글 상세 조회
		Map<String, Object> questionDetail = questionMapper.questionDetail(param);
		if (questionDetail == null) {
			resultMap.put("success", false);
			resultMap.put("message", "게시글을 찾을 수 없습니다.");
			return resultMap;
		}

		// 2. 권한 체크 (작성자만 수정 가능)
		String dbAuthor = (String) questionDetail.get("author");
		String username = (String) param.get("username");
		if (!username.equals(dbAuthor)) {
			resultMap.put("success", false);
			resultMap.put("message", "수정 권한이 없습니다.");
			return resultMap;
		}

		// 3. 수정 파라미터 준비
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", param.get("id"));
		paramMap.put("subject", param.get("subject"));
		paramMap.put("content", param.get("content"));

		// 4. DB 업데이트
		int updateResult = questionMapper.updateQuestion(paramMap);
		if (updateResult != 1) {
			resultMap.put("success", false);
			resultMap.put("message", "게시글 수정에 실패했습니다.");
			return resultMap;
		}

		resultMap.put("success", true);
		resultMap.put("message", "게시글이 성공적으로 수정되었습니다.");
		return resultMap;
	}

	// 글 삭제
	@Transactional(readOnly = false)
	public Map<String, Object> deleteQuestion(Map<String, Object> param) throws Exception {
		log.info("Delete QuestionInfo: {}", param);
		Map<String, Object> resultMap = new HashMap<>();

		// 1. 게시글 상세 조회
		Map<String, Object> questionDetail = questionMapper.questionDetail(param);
		if (questionDetail == null) {
			resultMap.put("success", false);
			resultMap.put("message", "게시글을 찾을 수 없습니다.");
			return resultMap;
		}

		// 2. 권한 체크 (작성자만 삭제 가능)
		String dbAuthor = (String) questionDetail.get("author");
		String username = (String) param.get("username");
		if (!username.equals(dbAuthor)) {
			resultMap.put("success", false);
			resultMap.put("message", "삭제 권한이 없습니다.");
			return resultMap;
		}

		// 3. 삭제 파라미터 준비
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("id", param.get("id"));

		// 4. DB 업데이트
		int deleteResult = questionMapper.deleteQuestion(paramMap);
		if (deleteResult != 1) {
			resultMap.put("success", false);
			resultMap.put("message", "게시글 삭제에 실패했습니다.");
			return resultMap;
		}

		resultMap.put("success", true);
		resultMap.put("message", "게시글이 성공적으로 삭제되었습니다.");
		return resultMap;
	}

	// 글 상세 조회
	@Transactional(readOnly = true)
	public Map<String, Object> getQuestionDetail(Map<String, Object> param) {
		log.info("QuestionInfo: {}", param);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<>();

		paramMap.put("id", param.get("id"));
		Map<String, Object> question_detail = questionMapper.questionDetail(paramMap);
		resultMap.put("questionDetail", question_detail);

		return resultMap;
	}

//	@Transactional(readOnly = true)
//	public Map<String, Object> getQuestionList(Map<String, Object> param){
//		log.info("QuestionInfo: {}", param);
//		Map<String, Object> resultMap = new HashMap<String, Object>();
//		Map<String, Object> paramMap = new HashMap<>();
//		
//		List<Map<String,Object>> questionList = questionMapper.selectQuestionList(paramMap);
//		resultMap.put("questionList", questionList);
//		log.info("questionList: {}", questionList);
//		return resultMap;
//	}

	@Transactional(readOnly = true)
	public Map<String, Object> getQuestionList(Map<String, Object> param) {
		log.info("QuestionInfo: {}", param);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<>();

		// TODO param에서 넘어온 검색값 검증
		String searchSubjectName;
		Integer pageNum;

		searchSubjectName = (String) param.get("searchSubjectName");
		pageNum = (Integer) param.get("pageNum");

		searchSubjectName = searchSubjectName != null ? searchSubjectName.trim() : "";
		pageNum = pageNum != null && pageNum > 0 ? pageNum : 1;
		
		log.info("searchSubjectName: {} ", searchSubjectName);
		log.info("pageNum: {} ", pageNum);

		// TODO paramMap에 넘길 값 전달
		int pageSize = 5;
		int offset = (pageNum - 1) * pageSize;

		paramMap.put("searchSubjectName", searchSubjectName);
		paramMap.put("pageNum", pageNum);
		paramMap.put("pageSize", pageSize);
		paramMap.put("offset", offset);

		int totalCount = questionMapper.selectQuestionTotalCount(paramMap);
		log.info("totalCount: {}", totalCount);
		resultMap.put("totalCount", totalCount);

		List<Map<String, Object>> question_list = questionMapper.selectQuestionList(paramMap);
		resultMap.put("question_list", question_list);
		log.info("question_list: {}", question_list);
		
		int firstPageNum = 1;
		int pageBlockSize = 3;
		int lastPageNum = (int) Math.ceil((double) totalCount / pageSize);
		int prevPageNum = pageNum > firstPageNum ? pageNum - 1 : firstPageNum;
		int nextPageNum = pageNum < lastPageNum ? pageNum + 1 : lastPageNum;
		int startBlockPage = ((pageNum - 1) / pageBlockSize) * pageBlockSize + 1;
		int endBlockPage = Math.min(startBlockPage + pageBlockSize - 1, lastPageNum);
		List<Integer> pageBlockList = new ArrayList<>();
		for (int i = startBlockPage; i <= endBlockPage; i++) {
			pageBlockList.add(i);
		}
		
		Map<String, Object> pagingMap = new HashMap<>();
		pagingMap.put("PAGE_BLOCK_SIZE", pageBlockSize);
		pagingMap.put("FIRST_PAGE_NUM", firstPageNum);
		pagingMap.put("LAST_PAGE_NUM", lastPageNum);
		pagingMap.put("PREV_PAGE_NUM", prevPageNum);
		pagingMap.put("NEXT_PAGE_NUM", nextPageNum);
		pagingMap.put("PAGE_BLOCK_LIST", pageBlockList);
		pagingMap.put("PAGE_NUM", pageNum); 
		pagingMap.put("PAGE_SIZE", pageSize);
		pagingMap.put("PAGE_OFFSET", offset);
		
		resultMap.put("pagingMap", pagingMap);

		return resultMap;
	}
}
