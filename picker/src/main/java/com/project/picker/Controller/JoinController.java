package com.project.picker.Controller;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.picker.DTO.MemberDTO;
import com.project.picker.Service.JoinService;

@Controller
public class JoinController {

	private static final Logger logger = LoggerFactory.getLogger(JoinController.class);
	
	@Inject
	JoinService jservice;
	
	// ���� ���� üũ ȭ��
	@RequestMapping(value="joinAgree", method= {RequestMethod.GET, RequestMethod.POST})
	public String joinAgree(Model model) {
		model.addAttribute("section", "join/JoinAgree.jsp");
		return "Index";
	}
	
	// ȸ������ �ۼ� ȭ��
	@RequestMapping(value="joinWrite", method= {RequestMethod.GET, RequestMethod.POST})
	public String joinWrite(MemberDTO mdto, Model model) {
		logger.info("���ǿ���1 : " + mdto.getM_terms());
		logger.info("���ǿ���2 : " + mdto.getM_personal());
		model.addAttribute("mdto", mdto);
		model.addAttribute("section", "join/JoinWrite.jsp");
		return "Index";
	}
	
	// ȸ������ ���̵� ��� ���� ���� üũ
	@RequestMapping(value="idChecking", method= {RequestMethod.GET, RequestMethod.POST}, produces="application/json; charset=utf-8")
	@ResponseBody
	public int idCheck(@RequestParam String m_id) {
		int result = jservice.idCheck(m_id);
		logger.info(">>> ���̵� ��� ���� ���� : "+result);
		return result;
	}
	
	// ȸ������ �Ϸ�
	@RequestMapping(value="joinSave", method= {RequestMethod.GET, RequestMethod.POST}, produces="application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> joinSave(MemberDTO mdto, Model model) {
		Map<String, Object> map = new HashMap<>();
		logger.info(">>> ȸ������ �Ϸ�");
		jservice.insertMember(mdto);
		jservice.insertJoinPoint(mdto.getM_id(), "�ű�ȸ�� ����������", 1000);
		map.put("msg", "success");
		return map;
	}
}
