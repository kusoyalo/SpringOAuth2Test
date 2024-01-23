package kuso.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import kuso.bean.VisitBean;
import kuso.entity.UserInfo;
import kuso.repository.UserInfoRepository;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@CrossOrigin
public class RoleSettingController{
	@Autowired
	private VisitBean visitBean;
	
	@Autowired
	private UserInfoRepository userInfoRepository;
	
	@PostMapping("/checkRole")
    public Map<String,Object> checkRole(){
		log.debug("RoleSettingController checkRole");
		
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		/*
		 * 判斷是否有角色權限可以進入此功能，目前只有"會員權限管理員"可以進入
		 * A：一般會員（一般國民）
		 * B：一般會員（內政部官員）
		 * C：知識庫管理員
		 * D：會員權限管理員
		*/
		List<String> roleList = visitBean.getRoleList();
		
		if(roleList.contains("D")){
			returnMap.put("verifyResult",true);
		}
		else{
			returnMap.put("verifyResult",false);
			returnMap.put("message","您不具有「會員權限管理員」的身份，無法進入");
		}
	    
		return returnMap;
    }
	
	@GetMapping("/goRoleSetting")
    public ModelAndView goRoleSetting(){
		log.debug("RoleSettingController goRoleSetting");
		
		ModelAndView modelAndView = new ModelAndView();
	    modelAndView.setViewName("roleSetting.html");
	    
		return modelAndView;
    }
	
	@PostMapping("/getRoleData")
    public List<UserInfo> getRoleData(){
		log.debug("RoleSettingController getRoleData");
		
		List<UserInfo> userInfoList = StreamSupport.stream(userInfoRepository.findAll().spliterator(),false)
		.collect(Collectors.toList());
	    
		return userInfoList;
    }
	
	@RequestMapping(value="/saveRoleData",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
	@ResponseBody
    public Map<String,Object> saveRoleData(@RequestBody List<Map<String,Object>> roleData){
		log.debug("RoleSettingController saveRoleData");
		
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		//全部的userInfo
		List<UserInfo> userInfoList = StreamSupport.stream(userInfoRepository.findAll().spliterator(),false)
				.collect(Collectors.toList());
		
		Gson gson = new Gson();
		
		//找出對應的資料，物件，然後改成新的role
		for(int x=0;x<roleData.size();x++){
			String loginType = (String)roleData.get(x).get("loginType");
			log.debug("loginType={}",loginType);
			
			String userID = (String)roleData.get(x).get("userID");
			log.debug("userID={}",userID);
			
			List<String> role = (List<String>)roleData.get(x).get("role");
			log.debug("role={}",role);
			
			//找出來的唯一物件
			Optional<UserInfo> userInfoOptional = userInfoList.stream().filter(z -> StringUtils.equals(loginType,z.getLoginType()) && StringUtils.equals(userID,z.getUserID())).findAny();
			
			if(userInfoOptional.isPresent()){
				//更新角色欄位
				userInfoOptional.get().setRole(gson.toJson(role));
			}
		}
		
		//將修改完後的全部物件更新回去
		userInfoRepository.saveAll(userInfoList);
		
		returnMap.put("message","修改完成");
	    
		return returnMap;
    }
}
