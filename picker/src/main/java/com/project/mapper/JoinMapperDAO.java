package com.project.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface JoinMapperDAO {

	// ���̵� ��� ���� ���� üũ
	@Select("SELECT COUNT(*) FROM picker_member WHERE m_id = #{m_id}")
	public int idCheck(@Param("m_id") String m_id);
}