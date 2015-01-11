package com.majun.test.web.redis;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

import com.majun.test.web.restful.UserDto;

public class RedisTest {

	public static void main(String[] args) {
		ApplicationContext context=new ClassPathXmlApplicationContext("spring/applicationContext.xml");
		RedisTemplate<Long, UserDto> redisTemplate = (RedisTemplate<Long, UserDto>)context.getBean("redisTemplate");
		UserDto user = new UserDto();
		user.setUserId(1101L);
		user.setUserName("majun");
		
		//添加用户
		redisTemplate.opsForValue().set(user.getUserId(), user);
		
		//查找用户
		UserDto user2 = redisTemplate.opsForValue().get(user.getUserId());
		System.out.println(user2);
		
		//修改用户
		user2.setUserName("majun2");
		redisTemplate.opsForValue().set(user.getUserId(), user2);
		System.out.println(redisTemplate.opsForValue().get(user.getUserId()));
		
		//删除用户
		redisTemplate.delete(user.getUserId());
		System.out.println(redisTemplate.opsForValue().get(user.getUserId()));
		
	}
	
}
