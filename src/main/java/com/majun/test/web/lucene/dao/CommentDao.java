package com.majun.test.web.lucene.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.majun.test.web.lucene.dto.CommentDto;

public class CommentDao {
	
	private String url = "jdbc:mysql://10.1.8.140:3306/test?user=root&password=testmysql";

	public CommentDao(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public List<CommentDto> findComments(){
		
		List<CommentDto> list = new ArrayList<CommentDto>();
		String sql = "select * from m_user mu ,m_comment mc where mu.id = mc.user_id";
		PreparedStatement pstmt = null;
		ResultSet result = null;
		try {
			Connection conn = DriverManager.getConnection(url);
			pstmt = conn.prepareStatement(sql);
			result =pstmt.executeQuery();
			while(result.next()){
				CommentDto dto = new CommentDto();
				dto.setUserId(result.getLong(1));
				dto.setUserName(result.getString(2));
				dto.setId(result.getLong(3));
				dto.setContent(result.getString(5));
				list.add(dto);
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
		return list;
	}
	
}
