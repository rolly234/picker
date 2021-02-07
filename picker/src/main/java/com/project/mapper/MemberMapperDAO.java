package com.project.mapper;

import java.util.ArrayList;
import java.util.Date;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.project.picker.DTO.BuyDTO;
import com.project.picker.DTO.BuyitemDTO;
import com.project.picker.DTO.LoginDTO;
import com.project.picker.DTO.MemberDTO;

@Repository
public interface MemberMapperDAO {

	// ȸ�� ���
	@Select("SELECT * FROM picker_member")
	public ArrayList<MemberDTO> memberList();
	
	// ȸ������ - �� ����
	@Select("SELECT * FROM picker_member WHERE m_id = #{m_id}")
	public MemberDTO viewMember(@Param("m_id") String m_id);
	
	// �α��� üũ
	@Select("SELECT m_id, m_password, m_name, m_type FROM picker_member WHERE m_id = #{m_id} AND m_password = #{m_password}")
	public MemberDTO loginCheck(LoginDTO ldto);
	
	// �α��� ���� ���� - �α��� ���� �� ���� id �� ��ȿ�ð� ���̺� ����
	@Select("UPDATE picker_member SET session_key = #{sessionId}, session_limit = #{sessionLimit} WHERE m_id = #{m_id}")
	public void loginRemember(@Param("sessionId") String sessionId, @Param("sessionLimit") Date sessionLimit, @Param("m_id") String m_id);
	
	// �α��� ���� ���� - ���� �α��� ����. ��ȿ�ð��� ���� �ְ� �ش� sessionId�� ������ ����� ����
	@Select("SELECT * FROM picker_member WHERE session_key = #{sessionId} and session_limit > sysdate")
	public MemberDTO getSessionUser(@Param("sessionId") String sessionId);
	
	// ��й�ȣ ��ġ ���� üũ
	@Select("SELECT COUNT(*) FROM picker_member WHERE m_id = #{m_id} AND m_password = #{m_password}")
	public int pwCheck(MemberDTO mdto);
	
	// ȸ��Ż��
	@Delete("DELETE FROM picker_member WHERE m_id = #{m_id}")
	public void deleteMember(@Param("m_id") String m_id);
	
	// ȸ��Ż�� - qna id ����
	@Update("UPDATE picker_qna SET m_id = '' WHERE m_id = #{m_id}")
	public void updateQna(@Param("m_id") String m_id);
	
	// ȸ��Ż�� - eval id ����
	@Update("UPDATE picker_eval SET m_id = '' WHERE m_id = #{m_id}")
	public void updateEval(@Param("m_id") String m_id);
	
	// ȸ��Ż�� - reply id ����
	@Update("UPDATE picker_reply SET m_id = '' WHERE m_id = #{m_id}")
	public void updateReply(@Param("m_id") String m_id);
	
	// ȸ��Ż�� - ����Ʈ ����
	@Delete("DELETE FROM picker_point WHERE m_id = #{m_id}")
	public void deletePoint(@Param("m_id") String m_id);
	
	// ȸ��Ż�� - ��ٱ��� ����
	@Delete("DELETE FROM picker_cart WHERE m_id = #{m_id}")
	public void deleteCart(@Param("m_id") String m_id);
	
	// ����Ʈ �Ѱ�
	@Select("SELECT SUM(p_point) FROM picker_point WHERE m_id = #{m_id}")
	public int onePoint(@Param("m_id") String m_id);
	
	// ���̵�ã�� - �̸���
	@Select("SELECT m_id FROM picker_member WHERE m_email = #{m_email}")
	public String findEmailId(@Param("m_email") String m_email);
	
	// ���̵� ã�� - �̸�, ����ó
	@Select("SELECT m_id FROM picker_member WHERE m_name = #{m_name} AND m_phone = #{m_phone}")
	public String findNamePhoneId(MemberDTO mdto);
	
	// ��й�ȣ ã�� - ���̵�
	@Select("SELECT m_password FROM picker_member WHERE m_id = #{m_id}")
	public String findIdPassword(@Param("m_id") String m_id);
	
	// ȸ���� ���� ��� ī��Ʈ
	@Select("SELECT COUNT(*) FROM picker_buy WHERE m_id = #{m_id}")
	public int getBuyCount(@Param("m_id") String m_id);
	
	// ȸ���� ����Ʈ ī��Ʈ
	@Select("SELECT COUNT(*) FROM picker_point WHERE m_id = #{m_id}")
	public int getPointCount(@Param("m_id") String m_id);

	// ���Ż�ǰ
	@Select("SELECT * FROM picker_buyitem ORDER BY bi_num DESC")
	public ArrayList<BuyitemDTO> buyItem();

	// ���Ż�1
	@Select("SELECT * FROM picker_buy WHERE m_id = #{m_id} AND b_code = #{b_code}")
	public BuyDTO oneBuyInfo(@Param("m_id") String m_id, @Param("b_code") String b_code);

	// ���Ż�2
	@Select("SELECT * FROM picker_buyitem WHERE b_code = #{b_code}")
	public ArrayList<BuyitemDTO> oneBuyItemInfo(@Param("b_code") String b_code);
	
	// ��ǰ���� �ݾ� �հ�
	@Select("SELECT SUM(bi_cnt*i_price) FROM picker_buyitem WHERE b_code = #{b_code}")
	public int sumBuyPrice(@Param("b_code") String b_code);

	// ��ȸ�� ���ų��� üũ
	@Select("SELECT * FROM picker_buy WHERE b_code = #{b_code} AND b_order_phone = #{b_order_phone}")
	public BuyDTO buyCheck(BuyDTO bdto);
	
	// ��ȸ�� ���Ż�1
	@Select("SELECT * FROM picker_buy WHERE b_code = #{b_code} AND b_order_phone = #{b_order_phone}")
	public BuyDTO noneOneBuyInfo(@Param("b_code") String b_code, @Param("b_order_phone") String b_order_phone);

	// ��ȸ�� ���Ż�2
	@Select("SELECT * FROM picker_buyitem WHERE b_code = #{b_code}")
	public ArrayList<BuyitemDTO> noneOneBuyItemInfo(@Param("b_code") String b_code);
}
