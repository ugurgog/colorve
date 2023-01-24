package com.example.colorve.model.db;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.validation.annotation.Validated;

import com.example.colorve.model.LocationModel;
import com.example.colorve.model.question.QuestionAnswer;

@Validated
@Document(collection = "user")
public class UserDTO implements Serializable{

	private static final long serialVersionUID = 8188095047174683812L;
	
	private String id;
	private String name;
	private String surname;
	
	@NotNull
	@Indexed(unique = true)
	private String email;
	private String status;
	private String password;
	private String colorName;
	private String colorHex;
	private String rgbValue;
	private String colorImageUrl;
	
	@Field("location")
    @BsonProperty("location")
	private LocationModel location;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getColorHex() {
		return colorHex;
	}
	public void setColorHex(String colorHex) {
		this.colorHex = colorHex;
	}
	public String getRgbValue() {
		return rgbValue;
	}
	public void setRgbValue(String rgbValue) {
		this.rgbValue = rgbValue;
	}
	public String getColorName() {
		return colorName;
	}
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	public String getColorImageUrl() {
		return colorImageUrl;
	}
	public void setColorImageUrl(String colorImageUrl) {
		this.colorImageUrl = colorImageUrl;
	}
	public LocationModel getLocation() {
		return location;
	}
	public void setLocation(LocationModel location) {
		this.location = location;
	}
}
