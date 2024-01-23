package kuso.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import kuso.bean.VisitBean;
import kuso.entity.UserInfo;
import kuso.repository.UserInfoRepository;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@CrossOrigin
public class LoginController{
	@Autowired
	private VisitBean visitBean;
	
	@Autowired
	private UserInfoRepository userInfoRepository;
	
	@PostMapping("/normalLogin")
    public Map<String,Object> normalLogin(@RequestParam String account,@RequestParam String pazzword){
		log.debug("LoginController normalLogin");
		
		log.debug("account={}",account);
		log.debug("pazzword={}",pazzword);
		
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put("result",false);
		
		UserInfo userInfo = userInfoRepository.getByTypeAndID("normal",account);
		
		//無此帳號存在
		if(userInfo == null){
			returnMap.put("message","帳號不存在或密碼錯誤");
			
			return returnMap;
		}
		
		//密碼錯誤
		if(!userInfo.getPazzword().equals(pazzword)){
			returnMap.put("message","帳號不存在或密碼錯誤");
			
			return returnMap;
		}
		
		returnMap.put("result",true);
		
		//set屬性到VisitBean SessionScope中，供之後使用
		visitBean.setLoginType("normal");
		visitBean.setUserID(account);
		
		Gson gson = new Gson();
		
		visitBean.setRoleList(gson.fromJson(userInfo.getRole(),ArrayList.class));
		
		//檢查是否有email，有的話，表示通過email驗證
		String email = userInfo.getEmail();
		log.debug("email={}",email);
		
		if(email == null){
			returnMap.put("needVerifyEmail",true);
			
			return returnMap;
		}
		
		visitBean.setEmail(email);
		
		returnMap.put("needVerifyEmail",false);
		
		return returnMap;
	}
}
