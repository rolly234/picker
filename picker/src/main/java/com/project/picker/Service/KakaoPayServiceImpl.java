package com.project.picker.Service;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.project.mapper.BuyMapperDAO;
import com.project.picker.DTO.BuyDTO;
import com.project.picker.DTO.BuyitemDTO;
import com.project.picker.DTO.KakaoPayApprovalDTO;
import com.project.picker.DTO.KakaoPayReadyDTO;
import com.project.picker.DTO.PointDTO;

@Service
public class KakaoPayServiceImpl implements KakaoPayService {

	private static final String HOST = "https://kapi.kakao.com";

	private KakaoPayReadyDTO kakaoPayReadyDTO;
	private KakaoPayApprovalDTO kakaoPayApprovalDTO;
	
	@Inject
	BuyMapperDAO bdao;

	String m_id;
	String tot;
	String getB_code;
	int b_code; // �������� select �� �ֹ��ڵ��� �ƽ���ȭ ����� 
	String [] i_code;
	String [] item_name;
	String [] sum_quantity;
	String [] item_price;
	int quantity = 0;
	int plus_point;
	int minus_point;
	
	
	@Override
	public String kakaoPayReady(HttpServletRequest request,HttpSession session, BuyDTO bdto) {
		
		String mincode; // �ּ� �ڵ�
		String maxcode; // �ִ� �ڵ�
		
		Date today = new Date();
		DateFormat format = new SimpleDateFormat("yyMMdd");
		System.out.println(format.format(today));

		mincode = format.format(today) + "0000";
		maxcode = format.format(today) + "9999";
		
		int minb_code = Integer.parseInt(mincode);
		int maxb_code = Integer.parseInt(maxcode);
		
		b_code = bdao.maxCode(minb_code, maxb_code);

		bdto.setB_code(b_code);
		getB_code = Integer.toString(bdto.getB_code());
	    // ------------------------------------------ ������� �ֹ��ڵ� ���ؼ� dto�� set �� ����ȯ
	    
		if(session.getAttribute("u_id") != null){
			m_id = (String) session.getAttribute("u_id");
		}else{
			m_id = "nonmember";
		}

	    tot = request.getParameter("tot");
	    int total = Integer.valueOf(tot);
	    int price = (int)(total*0.1);
	    String vat_amount = Integer.toString(price);
	    
		System.out.println("ready �̵�");
	    RestTemplate restTemplate = new RestTemplate();
	 
	    // ������ ��û�� Header
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Authorization", "KakaoAK " + "9c75223fdf027077aaee5d2e0c48507b");
	    headers.add("Accept", "application/x-www-form-urlencoded;charset=UTF-8");
	    headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
	    
	    // ������ ��û�� Body
	    MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
	    params.add("cid", "TC0ONETIME"); // ������ �ڵ�, 10�� (�׽�Ʈ�ڵ�)
	    params.add("partner_order_id", getB_code); // ������ �ֹ���ȣ, �ִ� 100�� -> �ֹ���ȣ (b_code) 202101160001
	    params.add("partner_user_id", m_id); // ������ ȸ�� id, �ִ� 100�� -> ȸ��ID (m_id)
	    
	    item_name = request.getParameterValues("item_name");
	    sum_quantity = request.getParameterValues("quantity");
	    i_code = request.getParameterValues("item_code");
	    item_price = request.getParameterValues("price_hidden");
	    
	    for(int i=0;i<item_name.length;i++) {
	    	int cnt = Integer.valueOf(sum_quantity[i].toString());
	    	quantity = quantity + cnt;
	    }
	    String sum_cnt = Integer.toString(quantity);
	    
	    // �� point�޼��带 ���� request��
	    
	    plus_point = +Integer.parseInt(request.getParameter("saving_point")); 
	    minus_point = -Integer.parseInt(request.getParameter("usePoint"));
	    System.out.println(plus_point);
	    System.out.println(minus_point);
	    
	    // ��ǰ���� �հ�� ��ǰ�� ������ �޾ƿ������� for�� �ۼ�
	    params.add("item_name", item_name[0] + " �� "+ (quantity-1)+"��"); // ��ǰ��, �ִ� 100�� -> ��ǰ��(i_name)
	    params.add("quantity", sum_cnt); // ��ǰ ���� (c_cnt)
	    params.add("total_amount", tot); // ��ǰ �Ѿ�
	    params.add("tax_free_amount", "0"); // ��ǰ ����� �ݾ�
	    params.add("vat_amount", vat_amount); // ��ǰ �ΰ��� �ݾ�
	    params.add("approval_url", "http://localhost:8090/picker/kakaoPaySuccess"); // ���� ���� �� redirect url, �ִ� 255��
	    params.add("cancel_url", "http://localhost:8090/picker/kakaoPayCancel"); // ���� ��� �� redirect url, �ִ� 255��
	    params.add("fail_url", "http://localhost:8090/picker/kakaoPaySuccessFail"); // ���� ���� �� redirect url, �ִ� 255��
	 
		HttpEntity<MultiValueMap<String, Object>> body = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
		System.out.println("body : "+body);
	    try {
	    		System.out.println("try");
	    	kakaoPayReadyDTO = restTemplate.postForObject(new URI(HOST + "/v1/payment/ready"), body, KakaoPayReadyDTO.class);
	    		System.out.println("VO : " + kakaoPayReadyDTO.getTid());
	    	
	    	//������           
	        return kakaoPayReadyDTO.getNext_redirect_pc_url();
	        
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    //���н�
	    return "/Pay";
	}

	@Override
	public KakaoPayApprovalDTO kakaoPayInfo(HttpServletRequest request, HttpSession session, BuyDTO bdto, String pg_token) {
		System.out.println("KakaoPayInfoVO............................................");
		System.out.println("-----------------------------");
		
		if(session.getAttribute("u_id") != null){
			m_id = (String) session.getAttribute("u_id");
		}else{
			m_id = "nonmember";
		}
		
		RestTemplate restTemplate = new RestTemplate();
	 
		// ������ ��û�� Header
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Authorization", "KakaoAK " + "9c75223fdf027077aaee5d2e0c48507b");
	    headers.add("Accept", "application/x-www-form-urlencoded;charset=UTF-8");
	    headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
	 
	    // ������ ��û�� Body
	    
	    System.out.println(kakaoPayReadyDTO.getTid());
	    System.out.println(getB_code);
	    System.out.println(m_id);
	    System.out.println(pg_token);
	    System.out.println(tot);
	    
	    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
	    params.add("cid", "TC0ONETIME");
	    params.add("tid", kakaoPayReadyDTO.getTid());
	    params.add("partner_order_id", getB_code);
	    params.add("partner_user_id", m_id);
	    params.add("pg_token", pg_token);
	    params.add("total_amount", tot);
	    
	    HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers);
	   
		    try {
		    	System.out.println(body);
				kakaoPayApprovalDTO = restTemplate.postForObject(new URI(HOST + "/v1/payment/approve"), body, KakaoPayApprovalDTO.class);
			    System.out.println("���� : " + kakaoPayApprovalDTO);
			      
			    return kakaoPayApprovalDTO;
			        
		    } catch (Exception e) {
			    	e.printStackTrace();
		    }
		    
	    return null;
	}
	
	
	@Override
	public void insertBuyitems(BuyDTO bdto, HttpServletRequest request, HttpSession session, Model model) {
		// picker_buyitem�� insert�� dto�� set �۾�----------------------------------------------------		
		
		ArrayList<BuyitemDTO> arr = new ArrayList<>();	
		for(int i=0; i<item_name.length; i++) {
			BuyitemDTO bidto = new BuyitemDTO();
			int bi_cnt = Integer.valueOf(sum_quantity[i].toString());
			int i_price = Integer.valueOf(item_price[i].toString());
				bidto.setB_code(b_code);
				bidto.setI_img("imgs");
				bidto.setI_code(i_code[i]);
				bidto.setI_name(item_name[i]);
				bidto.setBi_cnt(bi_cnt);
				bidto.setI_price(i_price);
				arr.add(bidto);
		}
		for(int i=0;i<arr.size();i++) {
			bdao.insertBuyitem(arr.get(i));
		}
		
		
		// picker_buy�� insert�� dto�� set �۾�----------------------------------------------------
		bdto.setB_code(b_code);
		bdto.setB_order_name(request.getParameter("b_order_name"));
		bdto.setB_order_phone(request.getParameter("b_order_phone"));
		bdto.setB_order_email(request.getParameter("b_order_email"));
		bdto.setB_take_name(request.getParameter("b_take_name"));
		bdto.setB_take_email(request.getParameter("b_take_email"));
		bdto.setB_take_phone(request.getParameter("b_take_phone"));
		bdto.setB_take_zipcode(request.getParameter("b_take_zipcode"));
		bdto.setB_take_roadaddr(request.getParameter("b_take_roadaddr"));
		bdto.setB_take_detailaddr(request.getParameter("b_take_detailaddr"));
		bdto.setM_id(m_id);
		bdto.setB_price(Integer.parseInt(request.getParameter("b_price")));

		bdao.insertBuy(bdto);
		
	}

	@Override
	public void insertPlusPoint(PointDTO pdto) {
		pdto.setM_id(m_id);
		String p_history;
		int p_point = plus_point;
		if(p_point<0) {
			p_history = "���";
		}else {
			p_history = "����";
		}
		
		
		pdto.setP_history(p_history);
		pdto.setP_point(p_point);
		pdto.setB_code(b_code);
	}

	

}
