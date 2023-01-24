package com.example.colorve.model.db;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.validation.annotation.Validated;

import com.example.colorve.model.question.QuestionAnswer;

@Validated
@Document(collection = "questions")
public class QuestionDTO implements Serializable{

	private static final long serialVersionUID = 8188095047174683812L;
	
	private String id;
	private String question;
	private String status;
	private int groupId;
	
	@NotNull
	private int digit;
	
	@Field("answers")
    @BsonProperty("answers")
	private List<QuestionAnswer> answers;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<QuestionAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<QuestionAnswer> answers) {
		this.answers = answers;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getDigit() {
		return digit;
	}

	public void setDigit(int digit) {
		this.digit = digit;
	}
}
