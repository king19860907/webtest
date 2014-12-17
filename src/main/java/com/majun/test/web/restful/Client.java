package com.majun.test.web.restful;

import java.util.List;

import com.google.gson.Gson;
import com.majun.test.web.util.HttpConnectionManager;

public class Client {
	public static void main(String[] args) {
		
		HttpConnectionManager http = new HttpConnectionManager();
		List<String> results = http.doHttpGet("http://localhost/webtest/user/getUser.jspa");
		String jsonStr = "";
		for(String s : results){
			jsonStr+=s;
		}
		Gson gson = new Gson();
		UserDto dto = gson.fromJson(jsonStr, UserDto.class);
		System.out.println("用户ID："+dto.getUserId()+" 用户名:"+dto.getUserName());
	}
}
