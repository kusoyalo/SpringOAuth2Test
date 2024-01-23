package kuso.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class RegisterAccountController{
	@Autowired
	private VisitBean visitBean;
	
	@Autowired
	private UserInfoRepository userInfoRepository;
	
	@PostMapping("/checkAccount")
    public Map<String,Object> checkAccount(@RequestParam String account){
		log.debug("RegisterAccountController checkAccount");
		
		log.debug("account={}",account);
		
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put("result",true);
		
		UserInfo userInfo = userInfoRepository.getByTypeAndID("normal",account);
		
		//已經有同名的帳號存在
		if(userInfo != null){
			returnMap.put("result",false);
			
			returnMap.put("message","該帳號已經存在");
			
			return returnMap;
		}
		
		return returnMap;
	}
	
	@PostMapping("/registerAccount")
    public Map<String,Object> registerAccount(@RequestParam String account,@RequestParam String pazzword){
		log.debug("RegisterAccountController registerAccount");
		
		log.debug("account={}",account);
		log.debug("pazzword={}",pazzword);
		
		Map<String,Object> returnMap = new HashMap<String,Object>();
		//set屬性到VisitBean SessionScope中，供之後使用
		visitBean.setLoginType("normal");
		visitBean.setUserID(account);
		
		Gson gson = new Gson();
		
		UserInfo userInfo = new UserInfo();
		userInfo.setLoginType("normal");
		userInfo.setUserID(account);
		userInfo.setPazzword(pazzword);
		userInfo.setNeedChangePazz("N");
		
		//角色塞預設值A：一般會員（一般國民）
		List<String> roleList = new ArrayList<String>();
		roleList.add("A");
		
		userInfo.setRole(gson.toJson(roleList));
		
		userInfoRepository.save(userInfo);
		
		visitBean.setRoleList(roleList);
		
		returnMap.put("registerResult",true);
		
		return returnMap;
	}
}
