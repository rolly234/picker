package com.project.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.project.picker.DTO.BuyDTO;
import com.project.picker.DTO.BuyitemDTO;
import com.project.picker.DTO.ItemDTO;
import com.project.picker.DTO.MemberDTO;

@Repository 
public interface AdminMapperDAO {

	// �� ���� ȸ�������� �������� �Լ�	
	@Select("SELECT * FROM picker_member WHERE m_id = #{m_id} AND m_type = 1")
	public MemberDTO oneList(@Param("m_id") String m_id);	
	
	// ��ǰ ����Ʈ�� �������� �Լ�
	@Select("SELECT i_code,i_name,i_price,i_date,i_img,i_detailimg,i_category,i_chk FROM (SELECT I.*, ROWNUM AS rnum FROM (SELECT * FROM picker_item ORDER BY i_code DESC)I) WHERE rnum BETWEEN #{startRow} AND #{endRow}")
	public ArrayList<ItemDTO> itemList(@Param("startRow")  int startRow, @Param("endRow") int endRow);
	
	// �� ���� ��ǰ������ �������� �Լ�	
	@Select("SELECT * FROM picker_item WHERE i_code = #{i_code}")
	public ItemDTO oneItemList(@Param("i_code") String i_code);	
	
	// ��ǰ ������ �������� �Լ� - ����¡
	@Select("SELECT COUNT(*) FROM picker_item")
	public  int itemListCount();
	
	// �� ȸ����(�Ϲ�ȸ��) - ������ ������
	@Select("SELECT COUNT(*) FROM picker_member WHERE m_type = 1")
	public int memberCount();
	
	// �� ��ǰ�� - ������ ������
	@Select("SELECT COUNT(*) FROM picker_item")
	public int itemCount();
	
	// ��ǰ���
	@Insert("INSERT INTO picker_item VALUES(#{i_code}, #{i_name}, #{i_price}, SYSDATE, #{i_img}, #{i_detailimg}, #{i_category}, 0 )")
	public void ItemInsert(ItemDTO idto);
	
	//��ǰ�ڵ� �ִ밪 ���ϴ� ����
	@Select("SELECT MAX(i_code) FROM picker_item")
	public String getCode();
	
	// ��ü ȸ�� ����Ʈ ��� ī��Ʈ
	@Select("SELECT COUNT(SUM(a.p_point)) FROM picker_point a INNER JOIN picker_member b ON a.m_id = b.m_id WHERE b.m_type = '1' GROUP BY a.m_id")
	public int getAllPointCount();
	
	// ȸ���� ����Ʈ ����
	@Select("SELECT COUNT(*) FROM picker_point WHERE m_id = #{m_id}")
	public int getOnePointCount(@Param("m_id") String m_id);

	// ��ü ȸ�� ��� ī��Ʈ
	@Select("SELECT COUNT(*) FROM picker_member WHERE m_type IN (1, 2)")
	public int getAllMemberCount();

	// ��ü ȸ�� ��ȸ�� ���� ��� ī��Ʈ
	@Select("SELECT COUNT(*) FROM picker_buy")
	public int getAllBuyCount();

	// ��ü ȸ�� ��ȸ�� ���Ż�ǰ ��� ī��Ʈ
	@Select("SELECT * FROM picker_buyitem ORDER BY bi_num DESC")
	public ArrayList<BuyitemDTO> allBuyItem();
	
	// ��ü ȸ�� ��ȸ�� ���Ż�1
	@Select("SELECT * FROM picker_buy WHERE b_code = #{b_code}")
	public BuyDTO getOneBuyInfo(@Param("b_code") String b_code);

	// ��ü ȸ�� ��ȸ�� ���Ż�2
	@Select("SELECT * FROM picker_buyitem WHERE b_code = #{b_code}")
	public ArrayList<BuyitemDTO> getOneBuyItemInfo(@Param("b_code") String b_code);
	
	// ��ü ȸ�� ��ȸ�� ��ǰ���� �ݾ� �հ�
	@Select("SELECT SUM(bi_cnt*i_price) FROM picker_buyitem WHERE b_code = #{b_code}")
	public int getSumBuyPrice(@Param("b_code") String b_code);
	
}
