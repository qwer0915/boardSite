package com.boardSite.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.boardSite.user.SiteUser;

@Mapper
public interface SiteUserMapper {
	int insertUser(Map<String, Object> param);

	int checkDuplicateUsername(Map<String, Object> param);

	Map<String, Object> userInfo(Map<String, Object> param);
}
