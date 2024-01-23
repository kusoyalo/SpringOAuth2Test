package kuso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import kuso.bean.VisitBean;

@SpringBootApplication
@ServletComponentScan
public class Application extends SpringBootServletInitializer{

	public static void main(String[] args){
		SpringApplication.run(Application.class,args);
	}
	
	@Bean
	public VisitBean visitBean(){
		return new VisitBean();
	}
}
