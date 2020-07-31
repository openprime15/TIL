package kr.co.softcampus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {
	// "/" 이 주소로 get방식으로 들어오면 아래 클래스를 호출하겠다.	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home() {
		System.out.println("home");
		return "index";
	}
	
//	@RequestMapping(value = "/test1", method = RequestMethod.GET)
//	public String test1() {
//		System.out.println("test1");
//		return null;
//	}
	
}
