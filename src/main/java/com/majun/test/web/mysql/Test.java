package com.majun.test.web.mysql;


public class Test {

	public static void main(String[] args) {
		/*UserDto dto1 = new UserDto();
		dto1.setUserId(1L);
		dto1.setUserName("majun1");
		
		UserDto dto2 = new UserDto();
		dto2.setUserId(2L);
		dto2.setUserName("majun2");
		
		UserDao userDao = new UserDao();
		userDao.insert(dto1);
		userDao.insert(dto2);*/
		
		UserDao userDao = new UserDao();
		System.out.println(userDao.getUserById(1L));
		System.out.println(userDao.getUserById(2L));
		System.out.println(userDao.getUserById(3L));
	}
	
}
