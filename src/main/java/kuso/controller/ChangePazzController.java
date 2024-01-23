package kuso.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kuso.bean.VisitBean;
import kuso.entity.UserInfo;
import kuso.repository.UserInfoRepository;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@CrossOrigin
public class ChangePazzController{
	@Autowired
	private VisitBean visitBean;
	
	@Autowired
	private UserInfoRepository userInfoRepository;
	
	@PostMapping("/checkNeedChangePazz")
    public Map<String,Object> checkNeedChangePazz(){
		log.debug("ChangePazzController checkNeedChangePazz");
		
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		UserInfo userInfo = userInfoRepository.getByTypeAndID(visitBean.getLoginType(),visitBean.getUserID());
		
		returnMap.put("needChangePazz",userInfo.getNeedChangePazz());
		
		return returnMap;
	}
	
	@PostMapping("/changePazz")
    public Map<String,Object> changePazz(@RequestParam String originalPazz,@RequestParam String newPazz){
		log.debug("ChangePazzController changePazz");
		
		log.debug("originalPazz={}",originalPazz);
		log.debug("newPazz={}",newPazz);
		
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put("result",false);
		
		UserInfo userInfo = userInfoRepository.getByTypeAndID(visitBean.getLoginType(),visitBean.getUserID());
		
		if(!userInfo.getPazzword().equals(originalPazz)){
			returnMap.put("message","原密碼錯誤");
			
			return returnMap;
		}
		
		userInfo.setPazzword(newPazz);
		userInfo.setNeedChangePazz("N");
		
		userInfoRepository.save(userInfo);
		
		returnMap.put("result",true);
		
		return returnMap;
	}
}
