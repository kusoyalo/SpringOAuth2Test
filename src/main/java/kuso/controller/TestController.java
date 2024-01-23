package kuso.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@CrossOrigin
/**
 * 測試前端打過來
 */
public class TestController{
	
	@PostMapping("/testReturnMap")
    public Map<String,Object> testReturnMap(){
		log.debug("TestController testReturnMap");
		
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put("result",true);
		returnMap.put("message","回傳的訊息");
		
		return returnMap;
	}
	
	@PostMapping("/testWithParam")
    public Map<String,Object> testWithParam(@RequestParam String param,@RequestParam String param2){
		log.debug("TestController testWithParam");
		
		log.debug("param={}",param);
		log.debug("param2={}",param2);
		
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put("result",true);
		returnMap.put("param",param);
		returnMap.put("param2",param2);
		returnMap.put("message","回傳的訊息");
		
		return returnMap;
	}
	
	@PostMapping("/testError")
    public void testError() throws Exception{
		log.debug("TestController testError");
		
		throw new Exception("這是一個Exception");
	}
}
