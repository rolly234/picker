package com.project.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.project.picker.DTO.ItemDTO;

@Repository 
public interface ItemMapperDAO {
	
	// �Ű����� @Param �۾� �� ��!
	
	// ī�װ� �� ���ı��� �����ؼ� ����Ʈ �ҷ����� �Լ�
	@Select("SELECT * FROM picker_item WHERE i_category = #{i_category} ORDER BY #{option}")
	public ArrayList<ItemDTO> listBy( @Param("i_category") String i_category, @Param("option") String option);
	
	// ī�װ� ���� ���� ����Ʈ�� �ҷ����� �Լ�
	@Select("SELECT * FROM picker_item WHERE i_category = #{i_category}")
	public ArrayList<ItemDTO> ItemList(@Param("i_category") String i_category);
	
	// ��ǰ ������ �ҷ����� �Լ�
	@Select("SELECT * FROM picker_item WHERE i_code = #{i_code}")
	public ItemDTO itemView(@Param("i_code") String i_code);
	
	// ī�װ� �ҷ����� �Լ�
		@Select("SELECT DISTINCT(i_category) FROM picker_item where i_category=#{i_category }")
		public ItemDTO cateName(@Param("i_category") String i_category);

}
