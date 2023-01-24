package com.example.colorve.service;

import java.util.List;

import com.example.colorve.model.BaseResponseModel;
import com.example.colorve.model.db.QuestionDTO;

public interface IQuestionService {
	
	BaseResponseModel<List<QuestionDTO>> listAllQuestions(String status, Integer groupId, Integer digit);
	
	BaseResponseModel<List<QuestionDTO>> listQuestionsToAnswer();
	
	BaseResponseModel<QuestionDTO> getQuestionById(String id);
	
	BaseResponseModel<QuestionDTO> addOrUpdateQuestion(QuestionDTO user);
	
	@SuppressWarnings("rawtypes")
	BaseResponseModel deleteQuestion(String id);
}
