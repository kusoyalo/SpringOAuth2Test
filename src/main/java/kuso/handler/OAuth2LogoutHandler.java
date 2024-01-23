package kuso.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class OAuth2LogoutHandler implements LogoutHandler{
	public void logout(HttpServletRequest request,HttpServletResponse response,Authentication authentication){
		log.debug("OAuth2LogoutHandler logout");
		
	}
}
