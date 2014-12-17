package com.majun.test.web.restful;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@RequestMapping("/user" )
@Controller
public class UserController {
	
	@RequestMapping(method=RequestMethod.GET,value="/getUser")
	@ResponseBody
	public String getUser(){
		UserDto dto = new UserDto();
		dto.setUserId(10001L);
		dto.setUserName("majun");
		Gson gson = new Gson();
		return gson.toJson(dto);
	}
	
}
