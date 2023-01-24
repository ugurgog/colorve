package com.example.colorve.model.db;

import java.io.Serializable;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.validation.annotation.Validated;

import com.example.colorve.model.question.QuestionAnswer;

@Validated
@Document(collection = "user_question_answers")
public class UserQuestionAnswersDTO implements Serializable{

	private static final long serialVersionUID = 8188095047174683812L;
	
	private String id;
	private String questionId;
	private String userId;
	private int questionGroupId;
	private int questionGroupDigit;
	
	@Field("answer")
    @BsonProperty("answer")
	private QuestionAnswer answer;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public QuestionAnswer getAnswer() {
		return answer;
	}

	public void setAnswer(QuestionAnswer answer) {
		this.answer = answer;
	}

	public int getQuestionGroupId() {
		return questionGroupId;
	}

	public void setQuestionGroupId(int questionGroupId) {
		this.questionGroupId = questionGroupId;
	}

	public int getQuestionGroupDigit() {
		return questionGroupDigit;
	}

	public void setQuestionGroupDigit(int questionGroupDigit) {
		this.questionGroupDigit = questionGroupDigit;
	}
}
