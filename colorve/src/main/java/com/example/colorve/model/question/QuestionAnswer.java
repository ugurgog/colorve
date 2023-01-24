package com.example.colorve.model.question;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class QuestionAnswer implements Serializable{

	private static final long serialVersionUID = 8188095047174683812L;
	
	@NotNull
	private String answer;
	
	@NotNull
	private int value;
	
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
}
