package com.project.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.project.picker.DTO.CartDTO;

@Repository
public interface CartMapperDAO {

/*	@Select("SELECT * FROM picker_cart WHERE m_id is null ORDER BY c_num")
	public ArrayList<CartDTO> nullCartList(); // ȸ�� ���̵� null�� ��ٱ��� ��ü ���
*/	
	@Select("SELECT * FROM picker_cart WHERE m_id = #{m_id} ORDER BY c_num")
	public ArrayList<CartDTO> allCartList(@Param("m_id") String m_id); // ȸ�� ���̵� ��ٱ��� ��ü ���
	
	@Select("SELECT NVL(MAX(c_num)+1, 1) FROM picker_cart")
	public int getNum();
	
	@Insert("INSERT INTO picker_cart (c_num, i_img ,i_code, i_name, c_cnt, i_price, m_id) VALUES(#{c_num}, #{i_img}, #{i_code}, #{i_name}, #{c_cnt}, #{i_price}, #{m_id})")
	public void insertCart(CartDTO cdto); // ��ٱ��Ͽ� ��ǰ ����

	@Select("SELECT count(*) FROM picker_cart WHERE i_code = #{i_code} AND m_id= #{m_id}")
	public int cartCount(CartDTO cdto); // ȸ���� �ڵ尡 ���� ���� ī��Ʈ ���
	
	@Update("UPDATE picker_cart SET c_cnt = #{c_cnt} + c_cnt WHERE i_code = #{i_code} AND m_id= #{m_id}")
	public void cartCntUpdate(CartDTO cdto); // �ڵ尡 ������ ��ٱ��� ���� ������Ʈ
	
	@Update("UPDATE picker_cart SET c_cnt = #{c_cnt} + c_cnt WHERE c_num = #{c_num}")
	public void cartCntUpdate_Non(@Param("c_cnt") int c_cnt, @Param("c_num") int c_num); // �ڵ尡 ������ ��ٱ��� ���� ������Ʈ
	
	@Delete("DELETE FROM picker_cart WHERE m_id = #{m_id} AND i_code = #{i_code}")
	public void delOne(@Param("m_id") String m_id, @Param("i_code") String i_code); // ��ٱ��� üũ�ڽ� ���õ� ��ǰ�� ȸ�� ���̵�� �ڵ尡 ������ ���� 
	
	@Delete("DELETE FROM picker_cart WHERE c_num = #{c_num}")
	public void delOne_Non(@Param("c_num") int c_num); // ��ٱ��� üũ�ڽ� ���õ� ��ǰ�� ��ٱ��Ϲ�ȣ�� ���� ��ȣ�� ������ ���� 
	
	@Delete("DELETE FROM picker_cart WHERE m_id = #{m_id}")
	public void delAll(@Param("m_id") String m_id); // ȸ�� ��ٱ��� ��ü����
	
	@Delete("DELETE FROM picker_cart WHERE c_num = #{c_num}")
	public void delAll_Non(@Param("c_num") int c_num); // ��ȸ�� ��ٱ��� ��ü����
	
	@Select("SELECT count(*) FROM picker_cart WHERE m_id = #{m_id}")
	public int totalCartCount(@Param("m_id") String m_id); // ��ٱ��� ȸ�����̵� ī��Ʈ�� ���
	
	@Update("UPDATE picker_cart SET m_id=#{m_id} WHERE c_num = #{c_num}")
	public void changeId(@Param("m_id") String m_id, @Param("c_num") int c_num); // īƮ ��ȣ�� ������ ���ǿ� �α��ε� ���̵�� ������Ʈ
	
	@Update("UPDATE picker_cart SET c_cnt = #{c_cnt} + c_cnt WHERE i_code = #{i_code} AND m_id = #{m_id}") //���̵�� �ڵ尡 ��ġ�ϸ� ���� ������Ʈ
	public void updateCnt(@Param("c_cnt") int c_cnt,@Param("i_code") String i_code,@Param("m_id") String m_id);
	
	@Update("UPDATE picker_cart SET c_cnt = #{c_cnt} WHERE i_code = #{i_code} AND m_id = #{m_id}") //���̵�� �ڵ尡 ��ġ�ϸ� ���� ������Ʈ
	public void plmaupdateCnt(CartDTO cdto);
	
	@Update("UPDATE picker_cart SET c_cnt = #{c_cnt} WHERE c_num = #{c_num}") //���̵�� �ڵ尡 ��ġ�ϸ� ���� ������Ʈ
	public void plmaupdateCnt_Non(CartDTO cdto);
	
	@Delete("DELETE FROM picker_cart WHERE c_num = #{c_num}")
	public void delOriginList(@Param("c_num") int c_num);

	@Select("SELECT * FROM picker_cart WHERE i_code = #{i_code}")
	public CartDTO getCartItem(@Param("i_code") String i_code);
	
	@Select("SELECT * FROM picker_cart WHERE c_num = #{c_num}")
	public CartDTO getCartNum(@Param("c_num") int c_num);
	
	
}
