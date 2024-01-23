package kuso.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import kuso.bean.VisitBean;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class VisitBeansUtil{
	private List<VisitBean> visitBeans;
	
	public VisitBean findVisitBeanByTypeAndID(String loginType,String userID){
		visitBeans = Optional.ofNullable(visitBeans).orElseGet(ArrayList<VisitBean> :: new);
		
		if(visitBeans.isEmpty()){
			return null;
		}
		
		VisitBean visitBean = visitBeans.stream().filter(x -> StringUtils.equals(loginType,x.getLoginType()) && StringUtils.equals(userID,x.getUserID())).findAny().orElseGet(null);
		return visitBean;
	}
	
	public void updateVisitBean(VisitBean visitBean){
		visitBeans = Optional.ofNullable(visitBeans).orElseGet(ArrayList<VisitBean> :: new);
		
		if(visitBeans.isEmpty()){
			visitBeans.add(visitBean);
			
			return;
		}
		
		Optional<VisitBean> visitBeanOptional = visitBeans.stream().filter(x -> StringUtils.equals(visitBean.getLoginType(),x.getLoginType()) && StringUtils.equals(visitBean.getUserID(),x.getUserID())).findAny();
		
		if(visitBeanOptional.isPresent()){
			visitBeans.remove(visitBeanOptional.get());
		}
		
		visitBeans.add(visitBean);
	}

	public void removeVisitBean(VisitBean visitBean){
		visitBeans = Optional.ofNullable(visitBeans).orElseGet(ArrayList<VisitBean> :: new);
		
		if(visitBeans.isEmpty()){
			return;
		}
		
		Optional<VisitBean> visitBeanOptional = visitBeans.stream().filter(x -> StringUtils.equals(visitBean.getLoginType(),x.getLoginType()) && StringUtils.equals(visitBean.getUserID(),x.getUserID())).findAny();
		
		if(visitBeanOptional.isPresent()){
			visitBeans.remove(visitBeanOptional.get());
		}
	}
}
