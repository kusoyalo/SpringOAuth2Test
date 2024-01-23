package kuso.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;

import kuso.bean.VisitBean;
import kuso.entity.UserInfo;
import kuso.repository.UserInfoRepository;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@CrossOrigin
public class OAuth2Controller{
	@Autowired
	private VisitBean visitBean;
	
	@Autowired
	private UserInfoRepository userInfoRepository;
	
	@PostMapping("/oauth2Login")
    public Map<String,Object> oauth2Login(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient oauth2AuthorizedClient,@AuthenticationPrincipal OAuth2User oauth2User,@AuthenticationPrincipal OidcUser oidcUser){
		log.debug("OAuth2Controller oauth2Login");
		
		//目前只有Google會回傳原生的token
		String tokenValue = oidcUser == null ? null : oidcUser.getIdToken() == null ? null : oidcUser.getIdToken().getTokenValue();
		log.debug("tokenValue={}",tokenValue);
		
		//用registrationId判斷是哪種登入
		String registrationId = oauth2AuthorizedClient.getClientRegistration().getRegistrationId();
		log.debug("registrationId={}",registrationId);
		
		String userID = oauth2User.getName();
		log.debug("userID={}",userID);
		
		//set屬性到VisitBean SessionScope中，供之後使用
		visitBean.setLoginType(registrationId);
		visitBean.setUserID(userID);
		
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put("loginType",registrationId);
		returnMap.put("userID",userID);
		
		//查詢是否有使用者資料在DB
		UserInfo userInfo = userInfoRepository.getByTypeAndID(registrationId,userID);
		
		Gson gson = new Gson();
		
		//沒資料的話就新增到DB
		if(userInfo == null){
			userInfo = new UserInfo();
			userInfo.setLoginType(registrationId);
			userInfo.setUserID(userID);
			
			userInfo.setNeedChangePazz("N");
			
			//角色塞預設值A：一般會員（一般國民）
			List<String> roleList = new ArrayList<String>();
			roleList.add("A");
			
			userInfo.setRole(gson.toJson(roleList));
			
			userInfoRepository.save(userInfo);
			
			visitBean.setRoleList(roleList);
			
			returnMap.put("needVerifyEmail",true);
			
			return returnMap;
		}
		
		visitBean.setRoleList(gson.fromJson(userInfo.getRole(),ArrayList.class));
		
		//有資料的話，檢查是否有email，有的話，表示通過email驗證
		String email = userInfo.getEmail();
		log.debug("email={}",email);
		
		if(email == null){
			returnMap.put("needVerifyEmail",true);
			
			return returnMap;
		}
		
		visitBean.setEmail(email);
		
		returnMap.put("needVerifyEmail",false);
		
		return returnMap;
		
//		if("github".equals(registrationId)){
//			returnMap.put("name",oauth2User.getAttribute("login"));
//		}
//		else if("google".equals(registrationId)){
//			returnMap.put("name",oauth2User.getAttribute("name"));
//		}
//		else if("line".equals(registrationId)){
//			returnMap.put("name",oauth2User.getAttribute("displayName"));
//		}
//		
//		return returnMap;
    }
	
	@PostMapping("/loginForGoogle")
    public Map<String,Object> loginForGoogle(@RequestParam String credential){
		log.debug("OAuth2Controller loginForGoogle");
		
		log.debug("credential={}",credential);
		
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),new GsonFactory()).build();
		
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put("result",true);
		
		GoogleIdToken idToken;
		
		try{
			idToken = verifier.verify(credential);
		}
		catch(Exception e){
			returnMap.put("result",false);
			returnMap.put("message","credential錯誤");
			
			return returnMap;
		}
		
		Payload payload = idToken.getPayload();

		String userID = payload.getSubject();
		log.debug("userID={}",userID);
		
		//set屬性到VisitBean SessionScope中，供之後使用
		visitBean.setLoginType("google");
		visitBean.setUserID(userID);
		
		//查詢是否有使用者資料在DB
		UserInfo userInfo = userInfoRepository.getByTypeAndID("google",userID);
		
		Gson gson = new Gson();
		
		//沒資料的話就新增到DB
		if(userInfo == null){
			userInfo = new UserInfo();
			userInfo.setLoginType("google");
			userInfo.setUserID(userID);
			
			userInfo.setNeedChangePazz("N");
			
			//角色塞預設值A：一般會員（一般國民）
			List<String> roleList = new ArrayList<String>();
			roleList.add("A");
			
			userInfo.setRole(gson.toJson(roleList));
			
			userInfoRepository.save(userInfo);
			
			visitBean.setRoleList(roleList);
			
			returnMap.put("needVerifyEmail",true);
			
			return returnMap;
		}
		
		visitBean.setRoleList(gson.fromJson(userInfo.getRole(),ArrayList.class));
		
		//有資料的話，檢查是否有email，有的話，表示通過email驗證
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
	
	@PostMapping("/loginForFB")
    public Map<String,Object> loginForFB(@RequestParam String userID){
		log.debug("OAuth2Controller loginForFB");
		
		log.debug("userID={}",userID);
		
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		//set屬性到VisitBean SessionScope中，供之後使用
		visitBean.setLoginType("facebook");
		visitBean.setUserID(userID);
		
		//查詢是否有使用者資料在DB
		UserInfo userInfo = userInfoRepository.getByTypeAndID("facebook",userID);
		
		Gson gson = new Gson();
		
		//沒資料的話就新增到DB
		if(userInfo == null){
			userInfo = new UserInfo();
			userInfo.setLoginType("facebook");
			userInfo.setUserID(userID);
			
			userInfo.setNeedChangePazz("N");
			
			//角色塞預設值A：一般會員（一般國民）
			List<String> roleList = new ArrayList<String>();
			roleList.add("A");
			
			userInfo.setRole(gson.toJson(roleList));
			
			userInfoRepository.save(userInfo);
			
			visitBean.setRoleList(roleList);
			
			returnMap.put("needVerifyEmail",true);
			
			return returnMap;
		}
		
		visitBean.setRoleList(gson.fromJson(userInfo.getRole(),ArrayList.class));
		
		//有資料的話，檢查是否有email，有的話，表示通過email驗證
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
