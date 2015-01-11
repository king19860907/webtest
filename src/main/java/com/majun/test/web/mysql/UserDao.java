package com.majun.test.web.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.majun.test.web.restful.UserDto;

public class UserDao {

	private String url = "jdbc:mysql://localhost:3306/test2?user=root";
	
	private int tb_num = 2;
	
	public UserDao(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void insert(UserDto user){
		PreparedStatement pstmt = null;
		try {
			Connection conn = DriverManager.getConnection(url);
			String sql = "insert into tb_user_"+user.getUserId()%tb_num+" (id,user_name) values (?,?)"; 
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, user.getUserId());
			pstmt.setString(2, user.getUserName());
			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				if(pstmt != null){
					pstmt.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public UserDto getUserById(Long userId){
		PreparedStatement pstmt = null;
		ResultSet result = null;
		try {
			Connection conn = DriverManager.getConnection(url);
			String sql = "select * from tb_user_"+userId%tb_num+" where id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, userId);
			result =pstmt.executeQuery();
			while(result.next()){
				UserDto dto = new UserDto();
				dto.setUserId(result.getLong(1));
				dto.setUserName(result.getString(2));
				return dto;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				if(pstmt != null){
					pstmt.close();
				}
				if(result != null){
					result.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return null;
		
	}
	
}
