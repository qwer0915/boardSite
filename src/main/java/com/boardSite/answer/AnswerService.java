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
    
    @Transactional(readOnly = false)
    public Map<String, Object> createAnswer(Map<String, Object> param) throws Exception {
    	log.info("param: {}", param);
    	Map<String, Object> result = new HashMap<>();

//        // 필수 필드 검증
//        if (!param.containsKey("content") || param.get("content") == null) {
//            throw new IllegalArgumentException("내용을 입력해주세요.");
//        }
//        if (!param.containsKey("questionId")) {
//            throw new IllegalArgumentException("질문 ID가 없습니다.");
//        }
//        if (!param.containsKey("author")) {
//            throw new IllegalArgumentException("사용자 정보가 없습니다.");
//        }
        // 질문 존재 여부 확인
        Map<String, Object> questionParam = new HashMap<>();
        questionParam.put("id", param.get("questionId"));
        if (questionMapper.questionDetail(questionParam) == null) {
            throw new IllegalArgumentException("존재하지 않는 질문입니다.");
        }

        // 답변 데이터 구성 (author는 username)
        Map<String, Object> insertParam = new HashMap<>();
        insertParam.put("content", param.get("content"));
        insertParam.put("createDate", LocalDateTime.now());
        insertParam.put("question", param.get("questionId"));
        insertParam.put("author", param.get("author")); // username

        int insertResult = answerMapper.insertAnswer(insertParam);
        if (insertResult != 1) {
        	System.out.println("실패...!");
            throw new RuntimeException("답변 저장에 실패했습니다.");
        }

        result.put("success", true);
        result.put("message", "답변이 성공적으로 등록되었습니다.");
        log.info("result: {}", result);
        return result;
    }
    
    
    // 댓글 수정
    @Transactional(readOnly = false)
    public Map<String, Object> updateAnswer(Map<String, Object> param) throws Exception {
    	log.info("param: {}", param);
    	Map<String, Object> result = new HashMap<>();

        // 질문 존재 여부 확인
        Map<String, Object> questionParam = new HashMap<>();
        questionParam.put("id", param.get("questionId"));
        if (questionMapper.questionDetail(questionParam) == null) {
            throw new IllegalArgumentException("존재하지 않는 질문입니다.");
        }

        // 답변 데이터 구성 (author는 username)
        Map<String, Object> insertParam = new HashMap<>();
        insertParam.put("content", param.get("content"));
        insertParam.put("createDate", LocalDateTime.now());

        int insertResult = answerMapper.insertAnswer(insertParam);
        if (insertResult != 1) {
        	System.out.println("실패...!");
            throw new RuntimeException("답변 수정에 실패했습니다.");
        }

        result.put("success", true);
        result.put("message", "답변이 성공적으로 수정되었습니다.");
        log.info("result: {}", result);
        return result;
    }
    
    
    
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAnswerList(Map<String, Object> param) {
    	log.info("param: {}", param);
        return answerMapper.selectAnswerList(param);
    }

}
