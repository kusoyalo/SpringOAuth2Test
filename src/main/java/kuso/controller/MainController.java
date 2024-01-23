package kuso.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import kuso.bean.VisitBean;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@CrossOrigin
public class MainController{
	@Autowired
	private VisitBean visitBean;
	
	@GetMapping("/goMain")
    public ModelAndView goMain(){
		log.debug("MainController goMain");
		
		ModelAndView modelAndView = new ModelAndView();
	    modelAndView.setViewName("main.html");
	    
		return modelAndView;
    }
	
	@PostMapping("/getUserInfo")
    public Map<String,Object> getUserInfo(){
		log.debug("MainController getUserInfo");
		
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put("loginType",visitBean.getLoginType());
		returnMap.put("userID",visitBean.getUserID());
		returnMap.put("email",visitBean.getEmail());
		returnMap.put("role",new Gson().toJson(visitBean.getRoleList()));
		
		return returnMap;
	}
}
