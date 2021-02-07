package com.project.picker.DAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.project.picker.DTO.BuyDTO;
import com.project.picker.DTO.MemberDTO;
import com.project.picker.DTO.PointDTO;

@Repository // ������ startup �� �� �� Ŭ������ �޸𸮿� �ڵ����� ��ϵ�
public class MemberDAOImpl implements MemberDAO {

	// �α� ó���� ���� ��ü ����
	private static final Logger logger = LoggerFactory.getLogger(MemberDAOImpl.class);
	
	// sqlSession ��ü�� �����ڰ� ���� �������� �ʰ� ���������� ������� ��
	@Inject // �������� ����
	SqlSession sqlSession;

	@Override
	public void updateMember(MemberDTO mdto) {
		if(mdto.getM_newpassword().equals("")) {
			logger.info(">>> �� ���� ����(��й�ȣ ����");
			sqlSession.selectOne("member.updateMember", mdto);
		}else {
			logger.info(">>> ��й�ȣ ����");
			sqlSession.selectOne("member.updatePassword", mdto);
		}
	}

	@Override
	public List<BuyDTO> buyList(String m_id, int startRow, int endRow) {
		logger.info(">>> ���� ����");
		Map<String, Object>map = new HashMap<>();
		map.put("m_id", m_id);
		map.put("startRow", startRow);
		map.put("endRow", endRow);
		return sqlSession.selectList("member.buyList", map);
	}
	
	@Override
	public List<PointDTO> pointList(String m_id, int startRow, int endRow) {
		logger.info(">>> ����Ʈ ����");
		Map<String, Object>map = new HashMap<>();
		map.put("m_id", m_id);
		map.put("startRow", startRow);
		map.put("endRow", endRow);
		return sqlSession.selectList("member.pointList", map);
	}

}
