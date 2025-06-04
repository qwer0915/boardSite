package com.boardSite.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.boardSite.question.Question;

@Mapper
public interface QuestionMapper {
	int insertQuestion(Map<String, Object> param);
	
	int updateQuestion(Map<String, Object> param);
	
	int deleteQuestion(Map<String, Object> param);
	
	Map<String, Object> questionDetail(Map<String, Object> param);
	
	int selectQuestionTotalCount(Map<String, Object> param);
	
	List<Map<String, Object>> selectQuestionList(Map<String, Object> param);
}
