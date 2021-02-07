package com.project.picker.Service;

import java.util.ArrayList;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.project.mapper.ItemMapperDAO;
import com.project.picker.DTO.ItemDTO;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Inject
	ItemMapperDAO idao;

	@Override
	public ArrayList<ItemDTO> listBy(String i_category, String option) {
		// option : ORDER BY �ڿ� ���� ������, ������, ��ϼ�
		// list?order=pricehigh list?order=pricelow list?order=date
		String queryoption = "";
		if(option.equals("pricehigh")) {queryoption = "i_price DESC";}
		else if(option.equals("pricelow")) {queryoption = "i_price ASC";}
		else if(option.equals("date")) {queryoption = "i_date DESC";}
		return idao.listBy(i_category, queryoption);
	}

	//�� ���� ��ǰ �ҷ����� �Լ�
	@Override
	public ItemDTO itemView(String i_code) {
		
		return idao.itemView(i_code);
	}

	//��ǰ ����Ʈ �ҷ����� �Լ�
	@Override
	public ArrayList<ItemDTO> ItemList(String i_category) {
		
		return idao.ItemList(i_category);
	}
	
	// �Ѱ��� ī�װ� �ҷ����� �Լ�
	@Override
	public ItemDTO cateName(String i_category) {
		
		return idao.cateName(i_category);
	}

}
