package com.example.colorve.service;

import java.util.List;

import com.example.colorve.model.BaseResponseModel;
import com.example.colorve.model.db.UserQuestionAnswersDTO;
import com.example.colorve.model.question.UserQuestionAnswerResponseModel;

public interface IUserQuestionAnswerService {
	
	BaseResponseModel<List<UserQuestionAnswersDTO>> findUserQuestionAnswersByUserId(String userId);
	
	BaseResponseModel<UserQuestionAnswersDTO> getUserQuestionAnswerById(String id);
	
	BaseResponseModel<UserQuestionAnswerResponseModel> addOrUpdateUserQuestionAnswer(UserQuestionAnswersDTO request);
}
