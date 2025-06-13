package com.boardSite.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AnswerMapper {
	int insertAnswer(Map<String, Object> param);

	int updateAnswer(Map<String, Object> param);
	
	int deleteAnswer(Map<String, Object> param);
	
	List<Map<String, Object>> selectAnswerList(Map<String, Object> param);
	
	Map<String, Object> answerDetail(Map<String, Object> param);
}
