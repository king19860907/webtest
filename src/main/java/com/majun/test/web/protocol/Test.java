package com.majun.test.web.protocol;

import com.google.protobuf.InvalidProtocolBufferException;
import com.majun.test.web.protocol.UserBuf.User.Builder;


public class Test {

	public static void main(String[] args) {
		//为类赋值
		Builder userBulider = UserBuf.User.newBuilder();
		userBulider.setUserId(10001);
		userBulider.setUserName("majun"); 
		
		//序列化
		UserBuf.User user = userBulider.build();
		byte[] bytes = user.toByteArray();
		System.out.println("序列化后的结果为："+bytes.toString());
		
		//反序列化
		try {
			UserBuf.User newUser = UserBuf.User.newBuilder().build().parseFrom(bytes);
			System.out.println("反序列化后结果:"+newUser);
		} catch (InvalidProtocolBufferException e) {
			// TODO Auto-generated catch block 
			e.printStackTrace(); 
		}
	}
	
}
