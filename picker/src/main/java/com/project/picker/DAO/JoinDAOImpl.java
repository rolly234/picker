package com.project.picker.DAO;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.project.picker.DTO.MemberDTO;

@Repository
public class JoinDAOImpl implements JoinDAO {

	private static final Logger logger = LoggerFactory.getLogger(JoinDAOImpl.class);
	
	@Inject
	SqlSession sqlSession;
	
	// ȸ������
	@Override
	public void insertMember(MemberDTO mdto) {
		logger.info(">>> ȸ������");
		sqlSession.insert("join.insertMember", mdto);
	}

	// ȸ�� ����Ʈ
	@Override
	public void insertJoinPoint(String m_id, String p_history, int p_point) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("m_id", m_id);
		map.put("p_history", p_history);
		map.put("p_point", p_point);
		sqlSession.insert("join.insertJoinPoint", map);
	}

}
