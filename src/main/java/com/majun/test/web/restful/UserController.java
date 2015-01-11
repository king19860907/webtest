package com.majun.test.web.restful;

import java.io.IOException;
import java.net.InetSocketAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.spy.memcached.MemcachedClient;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@RequestMapping("/user" )
@Controller
public class UserController {
	
	MemcachedClient client = null;
	
	UserController() throws IOException{
		client = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/getUser")
	@ResponseBody
	public String getUser(HttpServletRequest request){
		UserDto user = (UserDto)client.get(request.getSession().getId());
		if(user == null){
			return "error";
		}
		Gson gson = new Gson();
		return gson.toJson(user);
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/login")
	@ResponseBody
	public String login(HttpServletRequest request,HttpServletResponse response,String userName,String password){
		UserDto user = new UserDto();
		user.setUserId(System.currentTimeMillis());
		user.setUserName(userName);
		client.set(request.getSession().getId(), 1800, user);
		return "success";
	}
	
}
