package com.boardSite.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.boardSite.mapper.SiteUserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SiteUserService {
	private final SiteUserMapper siteUserMapper;

	// TODO 모든 데이터는 map으로 받는다, mapper에는 paramMap이라는 map을 던져 데이터 전송,
	// 결과는 resultMap에 담아서 던진다.

	// 회원 가입 처리
	@Transactional(readOnly = false)
	public Map<String, Object> createNewUser(Map<String, Object> param) throws Exception {
		log.info("JoinUserInfo: {}", param);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<>();

		// TODO 입력값 검증
		if (!param.containsKey("username") || !param.containsKey("password") || !param.containsKey("email")) {
			throw new Exception("@@@");
		}

		// TODO 아이디 중복 확인
		paramMap.put("username", param.get("username"));
		int duplicateCount;
		duplicateCount = siteUserMapper.checkDuplicateUsername(paramMap);
		if (duplicateCount > 0) {
			throw new Exception("!!!");
		}

		// TODO 회원가입
		paramMap.put("password", param.get("password"));
		paramMap.put("email", param.get("email"));
		int insertResult;
		insertResult = siteUserMapper.insertUser(paramMap);
		if (insertResult != 1) {
			throw new Exception("사용자 등록에 실패했습니다.");
		}
		resultMap.put("success", true);
		resultMap.put("message", "회원가입 성공");

		return resultMap;
	}

	// 로그인 처리
	@Transactional(readOnly = true)
	public Map<String, Object> loginByUserInfo(Map<String, Object> param) throws Exception {
		log.info("loginUserInfo: {}", param);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<>();

		// TODO 로그인 정보 검증
		if (!param.containsKey("username") || !param.containsKey("password")) {
			throw new Exception("@@@");
		}

		// TODO 회원 정보 조회
		paramMap.put("username", param.get("username"));
		paramMap.put("password", param.get("password"));
		Map<String, Object> userInfo = siteUserMapper.userInfo(paramMap);
		if (userInfo == null) {
			throw new Exception("!!!");
		}

		// TODO 회원 정보 비교
		String requsetPassword = (String) param.get("password");
		String dbPassword = (String) userInfo.get("password");
		if (!requsetPassword.equals(dbPassword)) {
			throw new Exception("###");
		}

		// TODO 로그인 성공
		resultMap.put("success", true);
		resultMap.put("message", "로그인 성공");
		resultMap.put("userInfo", userInfo);
		
		return resultMap;
	}
}
