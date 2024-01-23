package kuso.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import kuso.bean.VisitBean;
import kuso.entity.UserInfo;
import kuso.entity.VerifyEmail;
import kuso.repository.UserInfoRepository;
import kuso.repository.VerifyEmailRepository;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@CrossOrigin
public class EmailVerifyController{
	@Autowired
	private VisitBean visitBean;
	
	@Autowired
    private JavaMailSender javaMailSender;
	
	@Autowired
	private UserInfoRepository userInfoRepository;
	
	@Autowired
	private VerifyEmailRepository verifyEmailRepository;
	
	@GetMapping("/goEmailVerify")
    public ModelAndView goEmailVerify(){
		log.debug("EmailVerifyController goEmailVerify");
		
		ModelAndView modelAndView = new ModelAndView();
	    modelAndView.setViewName("emailVerify.html");
	    
		return modelAndView;
    }
	
	@PostMapping("/sendEmail")
    public Map<String,Object> sendEmail(@RequestParam String email){
		log.debug("EmailVerifyController sendEmail");
		
		log.debug("email={}",email);
		
		visitBean.setEmail(email);
		
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		//取亂數，六位數字
		int randomNumber = (int)(Math.random() * 1000000);
		log.debug("randomNumber={}",randomNumber);
		
		//限時5分鐘
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date()); 
		
		calendar.add(Calendar.MINUTE,5);
		
		Date expireDate = calendar.getTime();
		log.debug("expireDate={}",expireDate.toString());
		
		//存起來等使用者驗證
		VerifyEmail verifyEmail = new VerifyEmail();
		verifyEmail.setLoginType(visitBean.getLoginType());
		verifyEmail.setUserID(visitBean.getUserID());
		verifyEmail.setEmail(email);
		verifyEmail.setVerifyCode(String.valueOf(randomNumber));
		verifyEmail.setVerifyExpireDate(expireDate);
		
		verifyEmailRepository.save(verifyEmail);
		
		//會有錯誤javax.mail.AuthenticationFailedException: 535-5.7.8 Username and Password not accepted
		
//		SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(email);
//        message.setSubject("這是SpringOAuth2Test測試");
//        message.setText("kuku is coming");
//
//        javaMailSender.send(message);
		
		//跳過寄email的部份，先做假的
		returnMap.put("verifyCode",randomNumber);
		
		return returnMap;
	}
	
	@PostMapping("/verifyEmail")
    public Map<String,Object> verifyEmail(@RequestParam String verifyCode){
		log.debug("EmailVerifyController verifyEmail");
		
		log.debug("verifyCode={}",verifyCode);
		
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		//驗證數字是否正確，而且時間不能晚於expireDate
		VerifyEmail verifyEmail = verifyEmailRepository.getByTypeAndID(visitBean.getLoginType(),visitBean.getUserID());
		
		if(verifyEmail.getVerifyCode().equals(verifyCode) && verifyEmail.getVerifyExpireDate().compareTo(new Date()) >= 0){
			returnMap.put("verifyResult",true);
			
			//更新至UserInfo的email欄位
			UserInfo userInfo = userInfoRepository.getByTypeAndID(visitBean.getLoginType(),visitBean.getUserID());
			
			userInfo.setEmail(visitBean.getEmail());
			
			userInfoRepository.save(userInfo);
		}
		else if(!verifyEmail.getVerifyCode().equals(verifyCode)){
			returnMap.put("verifyResult",false);
			returnMap.put("message","驗證碼不符");
		}
		else if(verifyEmail.getVerifyExpireDate().compareTo(new Date()) < 0){
			returnMap.put("verifyResult",false);
			returnMap.put("message","超過驗證時間");
		}
		
		return returnMap;
	}
}
