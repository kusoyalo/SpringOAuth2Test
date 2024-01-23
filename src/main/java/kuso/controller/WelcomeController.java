package kuso.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@CrossOrigin
public class WelcomeController{
	@GetMapping({"/welcome"})
    public ModelAndView welcome(){
		log.debug("WelcomeController welcome");
		
		ModelAndView modelAndView = new ModelAndView();
	    modelAndView.setViewName("index.html");
	    return modelAndView;
    }
}
