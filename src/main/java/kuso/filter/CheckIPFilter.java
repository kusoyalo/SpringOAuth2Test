package kuso.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class CheckIPFilter extends OncePerRequestFilter{

	@Value("${internalIP}")
	private String internalIP;
	
	protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain) throws ServletException,IOException{
		log.debug("CheckIPFilter doFilterInternal");
		
		String URI = request.getRequestURI();
		log.debug("URI={}",URI);
		
		String ip = request.getRemoteAddr();
		log.debug("ip={}",ip);
		
		//要內網IP才能連
//		if(!URI.startsWith(internalIP)){
//			throw new ServletException("不是內網IP");
//		}
		
		filterChain.doFilter(request,response);
		
		return;
	}
	
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException{
		log.debug("CheckIPFilter shouldNotFilter");
		
		String URI = request.getRequestURI();
		log.debug("URI={}",URI);
		
		boolean byPass = false;
		
		//將webjars的需求排除檢核
		if(URI.startsWith("/SpringOAuth2Test/webjars")){
			byPass = true;
		}
		
        return byPass;
    }
}
