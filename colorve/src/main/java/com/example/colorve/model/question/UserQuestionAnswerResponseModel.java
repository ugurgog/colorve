package com.example.colorve.model.question;

import java.io.Serializable;

import com.example.colorve.model.db.UserQuestionAnswersDTO;

public class UserQuestionAnswerResponseModel implements Serializable{

	private static final long serialVersionUID = 8188095047174683812L;
	
	private UserQuestionAnswersDTO answer;
	private boolean completed;
	private String colorHexValue;
	private String colorName;
	private String colorImageUrl;
	private String rgbValue;
	
	public UserQuestionAnswersDTO getAnswer() {
		return answer;
	}
	public void setAnswer(UserQuestionAnswersDTO answer) {
		this.answer = answer;
	}
	public boolean isCompleted() {
		return completed;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	public String getColorHexValue() {
		return colorHexValue;
	}
	public void setColorHexValue(String colorHexValue) {
		this.colorHexValue = colorHexValue;
	}
	public String getColorName() {
		return colorName;
	}
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	public String getRgbValue() {
		return rgbValue;
	}
	public void setRgbValue(String rgbValue) {
		this.rgbValue = rgbValue;
	}
	public String getColorImageUrl() {
		return colorImageUrl;
	}
	public void setColorImageUrl(String colorImageUrl) {
		this.colorImageUrl = colorImageUrl;
	}
}
