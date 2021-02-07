package com.project.picker.Controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import com.project.picker.DTO.BuyDTO;
import com.project.picker.DTO.BuyitemDTO;
import com.project.picker.DTO.CartDTO;
import com.project.picker.DTO.LoginDTO;
import com.project.picker.DTO.MemberDTO;
import com.project.picker.DTO.PagingDTO;
import com.project.picker.DTO.PointDTO;
import com.project.picker.Service.CartService;
import com.project.picker.Service.MemberService;

@Controller
public class MemberController {

	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@Inject
	MemberService mservice;
	
	@Inject
	CartService Cservice;
	
	// �α��� ȭ��
	@RequestMapping(value="loginPage", method= {RequestMethod.GET, RequestMethod.POST})
	public String loginPage(HttpSession session, Model model, HttpServletRequest request) {
    	String referrer = request.getHeader("referer");
    	
    	if(referrer == null) {
    		model.addAttribute("msg", "�߸��� �����Դϴ�.");
    	}else {
    		
    		if(referrer.equals("http://localhost:8090/picker/loginPage") || referrer.equals("http://localhost:8090/picker/logout")) {
        		session.setAttribute("url", null);
        	}else {
        		session.setAttribute("url", referrer);
        	}
    	}
		model.addAttribute("section", "login/Login.jsp");
		return "Index";
	}
	
	// �α���
	@SuppressWarnings("unchecked")
	@RequestMapping(value="login", method= {RequestMethod.GET, RequestMethod.POST}, produces="application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> login(LoginDTO ldto, @RequestParam boolean log, Model model, 
			HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		MemberDTO mdto = mservice.loginCheck(ldto);
		Map<String, Object> map = new HashMap<>();
		
		if(mdto != null) {
			logger.info(">>> �Է� ���� ��ġ ����� ���� �α��� ����");
			session.setAttribute("login", mdto);
			session.setAttribute("u_id", mdto.getM_id());
			session.setAttribute("u_name", mdto.getM_name());
			session.setAttribute("u_type", mdto.getM_type());
			int count = Cservice.totalCartCount(mdto.getM_id());
			session.setAttribute("cnt", count);
			//session.setMaxInactiveInterval(10*2);
			
			if(log == true) { // �α��� ���� ������ üũ�� �� ��� ��Ű ����
				String sessionId = (String)session.getAttribute("u_id");
				Cookie cookie = new Cookie("loginCookie", sessionId); // ��Ű�� ���� id ����
				cookie.setPath("/"); // ��Ű�� ã�� ��θ� context ��η� ����
				int limitTime = 60 * 60 * 24 * 7; // 7��
				cookie.setMaxAge(limitTime); // ��Ű ��ȿ�ð� ����. ������ ��
				response.addCookie(cookie); // ��Ű ����
				
				Date sessionLimit = new Date(System.currentTimeMillis() + (1000 * limitTime)); // currentTimeMillis()�� 1/1000�� ����. 1000 ���ϴ� ��� �ʿ�
				mservice.loginRemember(sessionId, sessionLimit, mdto.getM_id()); // ���� ���� id�� ��ȿ�ð� DB ����
				logger.info(">>> �α��� ���� ��Ű ����");
			}
			
			if(session.getAttribute("sessionList") != null) {	
				ArrayList<CartDTO> list = (ArrayList<CartDTO>)session.getAttribute("sessionList");
				for(int i=0;i<list.size();i++) {
					list.get(i).setM_id(mdto.getM_id());
					if(Cservice.cartCount(list.get(i))==0) {
						Cservice.changeId(mdto.getM_id(),list.get(i).getC_num());
					}else{
						Cservice.updateCnt(list.get(i).getC_cnt(),list.get(i).getI_code(), mdto.getM_id());
						System.out.println(mdto.getM_id());
						System.out.println(list.get(i).getC_cnt());
						System.out.println(list.get(i).getI_code());
						System.out.println(list.get(i).getC_num());
						Cservice.delOriginList(list.get(i).getC_num());
					}
				}
				session.setAttribute("sessionList", null);
			}
			logger.info(">>> �α��� ����");
		}else {
			logger.info(">>> �Է� ���� ��ġ ����� ������");
			map.put("msg", "fail");
			logger.info(">>> �α��� ����");
		}
		return map;
	}
	
	// ���̵� ã��/��й�ȣ ã�� ȭ��
	@RequestMapping(value="findIdPw", method= {RequestMethod.GET, RequestMethod.POST})
	public String findIdPw(Model model) {
		model.addAttribute("section", "login/FindIdPw.jsp");
		return "Index";
	}
	
	// ���̵� ã�� ��� ȭ��
	@RequestMapping(value="findId", method= {RequestMethod.GET, RequestMethod.POST})
	public String findId(MemberDTO mdto, Model model) {
		if(mservice.findId(mdto) == null) {
			logger.info(">>> �������� �ʴ� ���̵�");
			model.addAttribute("msg", "��ġ�ϴ� ������ �����ϴ�.<br>�Է��Ͻ� ������ Ȯ�� �� �ٽ� �õ����ּ���.");
		}else {
			logger.info(">>> �����ϴ� ���̵�");
			model.addAttribute("m_id", mservice.findId(mdto));
		}
		return "login/FindId";
	}
	
	// ��й�ȣ ã�� ��� ȭ��
	@RequestMapping(value="findPw", method= {RequestMethod.GET, RequestMethod.POST})
	public String findPw(@RequestParam String m_id, Model model) {
		if(mservice.findPw(m_id) == null) {
			logger.info(">>> �������� �ʴ� ��й�ȣ");
			model.addAttribute("msg", "��ġ�ϴ� ������ �����ϴ�.<br>�Է��Ͻ� ������ Ȯ�� �� �ٽ� �õ����ּ���.");
		}else {
			logger.info(">>> �����ϴ� ��й�ȣ");
			model.addAttribute("m_password", mservice.findPw(m_id));
		}
		return "login/FindPw";
	}
	
	// �α׾ƿ�
	@RequestMapping(value="logout", method= {RequestMethod.GET, RequestMethod.POST})
	public String logout(HttpSession session, Model model, HttpServletRequest request, HttpServletResponse response) {
		Cookie loginCookie = WebUtils.getCookie(request, "loginCookie"); // �����ߴ� ��Ű
		
		if(loginCookie != null) { // ��Ű�� �����ϴ� ��� ��Ű ����
			String sId = (String)session.getAttribute("u_id");
			String sessionId = "none"; // DB ���� id �ʱ�ȭ�� ��� 
			MemberDTO mdto = new MemberDTO();
			mdto.setM_id(sId);
			loginCookie.setPath("/");
			loginCookie.setMaxAge(0); // ��ȿ�ð� 0���� �ʱ�ȭ
			response.addCookie(loginCookie); // ��Ű ����
			
			Date date = new Date(System.currentTimeMillis()); // ����ð�
			mservice.loginRemember(sessionId, date, mdto.getM_id()); // ��ȿ�ð��� ����ð����� ����
			logger.info(">>> �α׾ƿ� ��Ű ����");
		}
		// ���� �ʱ�ȭ
		session.setAttribute("u_id", null);
		session.setAttribute("u_name", null);
		session.setAttribute("u_type", null);
		session.setAttribute("cnt", null);
		logger.info(">>> ���� �ʱ�ȭ");
		
		model.addAttribute("section", "Section.jsp");
		return "Index";
	}
	
	// ��ȸ�� �ֹ���ȸ
	@RequestMapping(value="noneSearch", method= {RequestMethod.GET, RequestMethod.POST}, produces="application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> noneSearch(BuyDTO bdto, Model model, HttpSession session) {
		BuyDTO bdto1 = mservice.buyCheck(bdto);
		Map<String, Object> map = new HashMap<>();
		
		if(bdto1 != null) {
			logger.info(">>> �Է� ���� ��ġ ���ų��� ����");
			session.setAttribute("u_code", bdto1.getB_code());
			session.setAttribute("u_phone", bdto1.getB_order_phone());
			logger.info(">>> ��ȸ ����");
		}else {
			logger.info(">>> �Է� ���� ��ġ ���ų��� ������");
			map.put("msg", "fail");
			logger.info(">>> ��ȸ ����");
		}
		return map;
	}
	
	// ��ȸ�� ��ȸ ȭ��
	@RequestMapping(value="nonePage", method= {RequestMethod.GET, RequestMethod.POST})
	public String nonePage(HttpSession session, Model model, HttpServletRequest request) {
		String b_code = (String)session.getAttribute("u_code");
		String b_order_phone = (String)session.getAttribute("u_phone");
		BuyDTO bdto = mservice.noneOneBuyInfo(b_code, b_order_phone);
		ArrayList<BuyitemDTO> bidto = mservice.noneOneBuyItemInfo(b_code);
		int total = mservice.sumBuyPrice(b_code);
		model.addAttribute("bdto", bdto);
		model.addAttribute("bidto", bidto);
		model.addAttribute("total", total);
		model.addAttribute("section", "NonePage.jsp");
		return "Index";
	}
	
	// ��ȸ�� ��ȸ ȭ�� �ݱ�
	@RequestMapping(value="closed", method= {RequestMethod.GET, RequestMethod.POST})
	public String goIndex(HttpSession session) {
		session.setAttribute("u_code", null);
		session.setAttribute("u_phone", null);
		return "Index";
	}
	
	// �߸��� ����
	@RequestMapping(value="wrongAccess", method= {RequestMethod.GET, RequestMethod.POST})
	public String wrongAccess(Model model) {
		model.addAttribute("msg", "�߸��� �����Դϴ�.");
		model.addAttribute("loc", 3);
		return "Error";
	}
	
	// ���� �޽���
	@RequestMapping(value="errorPage", method= {RequestMethod.GET, RequestMethod.POST})
	public String errorPage(Model model) {
		model.addAttribute("msg", "�α��� �� �̿� �����մϴ�.");
		model.addAttribute("loc", 1);
		return "Error";
	}
	
	// ���������� ȭ��
	@RequestMapping(value="myPage", method= {RequestMethod.GET, RequestMethod.POST})
	public String myPage(Model model, HttpSession session) {
		String m_id = (String)session.getAttribute("u_id");
		int point = mservice.onePoint(m_id);
		logger.info(">>> ȸ�� ����Ʈ : "+point);
		model.addAttribute("point", point);
		model.addAttribute("section", "myPage/MyPage.jsp");
		return "Index";
	}
	
	// ȸ���� �ֹ� ���
	@RequestMapping(value="buyInfo", method= {RequestMethod.GET, RequestMethod.POST})
	public String buyInfo(@RequestParam int pageNum, Model model, HttpSession session) {
		String m_id = (String)session.getAttribute("u_id");
		ArrayList<BuyitemDTO> buyitem = mservice.buyItem();
		int cnt = mservice.getBuyCount(m_id);
		
		if(cnt > 0) {
			PagingDTO pgdto = new PagingDTO(pageNum, cnt, 10, 5);
			model.addAttribute("pgdto", pgdto);
			List<BuyDTO> buylist = mservice.buyList(m_id, pgdto.getStartRow(), pgdto.getEndRow());
			model.addAttribute("buylist", buylist);
		}
		model.addAttribute("buyitem", buyitem);
		return "myPage/BuyInfo";
	}
	
	// ȸ���� �ֹ� ��
	@RequestMapping(value="buyInfoDetail", method= {RequestMethod.GET, RequestMethod.POST})
	public String buyInfoDetail(@RequestParam String b_code, @RequestParam int pageNum, Model model, HttpSession session) {
		String m_id = (String)session.getAttribute("u_id");
		BuyDTO bdto = mservice.oneBuyInfo(m_id, b_code);
		ArrayList<BuyitemDTO> bidto = mservice.oneBuyItemInfo(b_code);
		int total = mservice.sumBuyPrice(b_code);
		model.addAttribute("bdto", bdto);
		model.addAttribute("bidto", bidto);
		model.addAttribute("total", total);
		model.addAttribute("pageNum", pageNum);
		return "myPage/BuyInfoDetail";
	}
	
	// ȸ���� �ֹ���� ���� �ֹ� ���
	@RequestMapping(value="buyCancel", method= {RequestMethod.GET, RequestMethod.POST})
	public String buyCancel(Model model, HttpSession session) {
		String m_id = (String)session.getAttribute("u_id");
		ArrayList<BuyDTO> buyCancelList = mservice.buyCancelList(m_id);
		ArrayList<BuyitemDTO> buyitem = mservice.buyItem();
		model.addAttribute("buyCancelList", buyCancelList);
		model.addAttribute("buyitem", buyitem);
		return "myPage/BuyCancel";
	}
	
	// ȸ���� �ֹ� ��
	@RequestMapping(value="buyCancelDetail", method= {RequestMethod.GET, RequestMethod.POST})
	public String buyCancelDetail(@RequestParam String b_code, Model model, HttpSession session) {
		String m_id = (String)session.getAttribute("u_id");
		BuyDTO bdto = mservice.oneBuyInfo(m_id, b_code);
		ArrayList<BuyitemDTO> bidto = mservice.oneBuyItemInfo(b_code);
		int total = mservice.sumBuyPrice(b_code);
		model.addAttribute("bdto", bdto);
		model.addAttribute("bidto", bidto);
		model.addAttribute("total", total);
		return "myPage/BuyCancelDetail";
	}
	
	// �� ���� ���� ȭ��
	@RequestMapping(value="myInfo", method= {RequestMethod.GET, RequestMethod.POST})
	public String myInfo(HttpSession session, Model model) {
		String m_id = (String)session.getAttribute("u_id");
		model.addAttribute("mdto", mservice.viewMember(m_id));
		return "myPage/MyInfo";
	}
	
	// �� ���� ����
	@RequestMapping(value="myInfoUpdate", method= {RequestMethod.GET, RequestMethod.POST}, produces="application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> myInfoUpdate(MemberDTO mdto, Model model) {
		Map<String, Object> map = new HashMap<>();
		
		if(mservice.pwCheck(mdto) == 0) {
			logger.info(">>> ��й�ȣ ����ġ");
			map.put("msg", "fail");
		}else {
			logger.info(">>> ��й�ȣ ��ġ �������� ����");
			mservice.updateMember(mdto);
		}
		return map;
	}
	
	// ȸ���� ����Ʈ ���
	@RequestMapping(value="pointInfo", method= {RequestMethod.GET, RequestMethod.POST})
	public String pointInfo(@RequestParam int pageNum, Model model, HttpSession session) {
		String m_id = (String)session.getAttribute("u_id");
		int cnt = mservice.getPointCount(m_id);
		
		if(cnt > 0) {
			PagingDTO pgdto = new PagingDTO(pageNum, cnt, 10, 5); // 10 10
			model.addAttribute("pgdto", pgdto);
			List<PointDTO> list = mservice.pointList(m_id, pgdto.getStartRow(), pgdto.getEndRow());
			model.addAttribute("list", list);
		}
		return "myPage/PointInfo";
	}
	
	// ȸ��Ż�� ȭ��
	@RequestMapping(value="withdrawPage", method= {RequestMethod.GET, RequestMethod.POST})
	public String withdrawPage() {
		return "myPage/WithdrawPage";
	}
	
	// ȸ��Ż��
	@RequestMapping(value="withdraw", method= {RequestMethod.GET, RequestMethod.POST}, produces="application/json; charset=utf-8")
	@ResponseBody
	public Map<String, Object> withdraw(MemberDTO mdto, Model model, HttpSession session) {
		String m_id = (String)session.getAttribute("u_id");
		mdto.setM_id(m_id);
		Map<String, Object> map = new HashMap<>();
		
		if(mservice.pwCheck(mdto) == 0) {
			logger.info(">>> ��й�ȣ ����ġ");
			map.put("msg", "fail");
		}else {
			logger.info(">>> ��й�ȣ ��ġ Ż�� ����");
			mservice.deleteMember(m_id);
			session.setAttribute("u_id", null);
			session.setAttribute("u_name", null);
			session.setAttribute("u_type", null);
		}
		return map;
	}
	
}
