package com.project.picker.DTO;

public class LoginDTO {

	private String m_id; // ���̵�
	private String m_password; // ��й�ȣ
	
	public LoginDTO() {}
	public LoginDTO(String m_id, String m_password) {
		this.m_id = m_id;
		this.m_password = m_password;
	}
	
	public String getM_id() {
		return m_id;
	}
	public void setM_id(String m_id) {
		this.m_id = m_id;
	}
	public String getM_password() {
		return m_password;
	}
	public void setM_password(String m_password) {
		this.m_password = m_password;
	}
	
	
}
