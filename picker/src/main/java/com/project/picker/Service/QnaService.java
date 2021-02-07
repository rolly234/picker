package com.project.picker.Service;

import java.util.ArrayList;

import com.project.picker.DTO.ItemDTO;
import com.project.picker.DTO.ItemQnaDTO;
import com.project.picker.DTO.QnaDTO;
import com.project.picker.DTO.ReplyDTO;

public interface QnaService {
	
	public int getCountByMember(String id); // ������ �Խñ� ���� ȣ��
	public int getCountByItem(String code); // ��ǰ�� �Խñ� ���� ȣ��
	public ArrayList<QnaDTO> getQnaListByMember(String id, int endrow); // ������ �Խñ� ��� ȣ��
	public ArrayList<ItemQnaDTO> getQnaListByItem(String code, int startRow, int endRow); // ��ǰ�� �Խñ� ��� ȣ��
	public ArrayList<ReplyDTO> getReplyByMember(ArrayList<QnaDTO> list); // ������ ��� ��� ȣ��
	public ArrayList<ReplyDTO> getReplyByQna(int num); // �Խñۺ� ��� ��� ȣ��
	public ItemDTO getItemInfo(String code); // �۾��� ȭ�� ��ǰ���� ȣ��
	public QnaDTO getQnaByNum(int num); // �Խñ� ȣ��
	public ItemQnaDTO getQnaWithReplyCount(int num); // �Խñ� �� ��� ���� ȣ��
	public boolean writeQna(QnaDTO dto); // �Խñ� ���
	public boolean modifyQna(QnaDTO dto); // �Խñ� ����
	public boolean deleteQna(int num); // �Խñ� ����
	public boolean writeReply(ReplyDTO dto, int ref); // ��� ���
	public void setAdminReplied(int num); // ������ ��� ���
	public String getReplyContent(int num); // ��� ���� ȣ��
	public boolean modifyReply(ReplyDTO dto); // ��� ����
	public boolean deleteReply(int num); // ��� ����

}
