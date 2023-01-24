package com.example.colorve.model.db;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "token_info")
public class TokenInfoDTO implements Serializable{

	private static final long serialVersionUID = 8188095047174683812L;
	
	private String id;
	private String token;
	private String site;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
}
