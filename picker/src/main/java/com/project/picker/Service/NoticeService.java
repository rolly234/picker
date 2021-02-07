package com.project.picker.Service;

import java.util.ArrayList;

import com.project.picker.DTO.NoticeDTO;

public interface NoticeService {
	
	public boolean writeInsert(NoticeDTO dto); // ������ �ۼ�
	public int getNoticeCount(String keyword); // ���� ���� ȣ��
	ArrayList<NoticeDTO> getNoticeList(String keyword, int startRow, int endRow); // ���� ����Ʈ ȣ��
	public void addCount(int num); // ������ ��ȸ�� ����
	public NoticeDTO getNoticeByNum(int num); // ������ ��ȸ(������ ���� ȣ��)
	public int getNext(int num); // ���� �� ȣ��
	public int getPrev(int num); // ���� �� ȣ��
	public String getTitle(int num); // ���� ȣ��
	public boolean modifyNotice(NoticeDTO dto); // ������ ����
	public boolean eraseNotice(int num); // ������ ����
	
}
