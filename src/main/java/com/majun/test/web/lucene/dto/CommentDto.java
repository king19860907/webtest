package com.majun.test.web.lucene.dto;

public class CommentDto {

	private Long id;
	
	private Long userId;
	
	private String userName;
	
	private String content;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "CommentDto [id=" + id + ", userId=" + userId + ", userName=" + userName + ", content=" + content + "]";
	}
	
}
