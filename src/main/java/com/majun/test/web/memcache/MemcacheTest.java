package com.majun.test.web.memcache;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.majun.test.web.restful.UserDto;

import net.spy.memcached.MemcachedClient;

public class MemcacheTest {

	public static void main(String [] args) throws IOException {
		MemcachedClient client = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
		
		//添加
		UserDto dto = new UserDto();
		dto.setUserId(1101L);
		dto.setUserName("majun");
		client.set(dto.getUserId().toString(), 3600, dto);
		
		//查询
		UserDto queryDto = (UserDto)client.get(dto.getUserId().toString());
		System.out.println(queryDto);
		
		//修改
		dto.setUserName("majun2");
		client.set(dto.getUserId().toString(), 3600, dto);
		System.out.println((UserDto)client.get(dto.getUserId().toString()));
		
		//删除
		client.delete(dto.getUserId().toString());
		System.out.println((UserDto)client.get(dto.getUserId().toString()));
	}
	
}
