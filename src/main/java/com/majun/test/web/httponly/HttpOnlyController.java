package com.majun.test.web.httponly;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/httponly" )
@Controller
public class HttpOnlyController {
	
	@RequestMapping(method=RequestMethod.GET,value="/http-only")
	@ResponseBody
	public String httpOnly(HttpServletRequest request,HttpServletResponse response){
		
		response.setHeader("Set-Cookie", "cookiename=test;Path=/;Domain=localhost;Max-age=3600;HTTPOnly");
		
		return "httponly-test";
	}

}
