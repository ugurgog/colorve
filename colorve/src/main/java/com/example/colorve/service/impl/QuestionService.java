package com.example.colorve.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.example.colorve.enums.ErrorTypeEnum;
import com.example.colorve.enums.StatusEnum;
import com.example.colorve.model.BaseResponseModel;
import com.example.colorve.model.db.QuestionDTO;
import com.example.colorve.model.question.QuestionAnswer;
import com.example.colorve.repository.QuestionRepository;
import com.example.colorve.service.IQuestionService;
import com.example.colorve.util.StringUtils;
import com.google.gson.Gson;

@Service
public class QuestionService implements IQuestionService{

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private Gson gson;
	
	@Override
	public BaseResponseModel<List<QuestionDTO>> listAllQuestions(String status, Integer groupId, Integer digit) {
		BaseResponseModel<List<QuestionDTO>> response = new BaseResponseModel<>();
		List<QuestionDTO> allQuestions = questionRepository.findAllByCriteria(status, groupId, digit);
		
		if(CollectionUtils.isEmpty(allQuestions))
			return new BaseResponseModel<>(ErrorTypeEnum.NO_CONTENT.name(), "No question founded!");
		
		response.setResult(allQuestions);
		response.setSuccess(true);
		return response;
	}

	@Override
	public BaseResponseModel<List<QuestionDTO>> listQuestionsToAnswer() {
		BaseResponseModel<List<QuestionDTO>> response = new BaseResponseModel<>();
		List<QuestionDTO> allQuestions = new ArrayList<>();
		
		List<QuestionDTO> firstQuestions = questionRepository.findQuestionsByGroupIdAndLimit(1, 4);
		
		if(CollectionUtils.isEmpty(firstQuestions) || firstQuestions.size() < 4)
			return new BaseResponseModel<>(ErrorTypeEnum.NO_CONTENT.name(), "First digit questions not enough!");

		List<QuestionDTO> secondQuestions = questionRepository.findQuestionsByGroupIdAndLimit(2, 4);
		
		if(CollectionUtils.isEmpty(secondQuestions) || secondQuestions.size() < 4)
			return new BaseResponseModel<>(ErrorTypeEnum.NO_CONTENT.name(), "Second digit questions not enough!");

		List<QuestionDTO> thirdQuestions = questionRepository.findQuestionsByGroupIdAndLimit(3, 4);
		
		if(CollectionUtils.isEmpty(thirdQuestions) || thirdQuestions.size() < 4)
			return new BaseResponseModel<>(ErrorTypeEnum.NO_CONTENT.name(), "Third digit questions not enough!");

		List<QuestionDTO> fourthQuestions = questionRepository.findQuestionsByGroupIdAndLimit(4, 4);
		
		if(CollectionUtils.isEmpty(fourthQuestions) || fourthQuestions.size() < 4)
			return new BaseResponseModel<>(ErrorTypeEnum.NO_CONTENT.name(), "Fourth digit questions not enough!");
		
		List<QuestionDTO> fifthQuestions = questionRepository.findQuestionsByGroupIdAndLimit(5, 4);
		
		if(CollectionUtils.isEmpty(fifthQuestions) || fifthQuestions.size() < 4)
			return new BaseResponseModel<>(ErrorTypeEnum.NO_CONTENT.name(), "Fifth digit questions not enough!");
		
		List<QuestionDTO> sixthQuestions = questionRepository.findQuestionsByGroupIdAndLimit(6, 4);
		
		if(CollectionUtils.isEmpty(sixthQuestions) || sixthQuestions.size() < 4)
			return new BaseResponseModel<>(ErrorTypeEnum.NO_CONTENT.name(), "Sixth digit questions not enough!");
		
		allQuestions.addAll(firstQuestions);
		allQuestions.addAll(secondQuestions);
		allQuestions.addAll(thirdQuestions);
		allQuestions.addAll(fourthQuestions);
		allQuestions.addAll(fifthQuestions);
		allQuestions.addAll(sixthQuestions);
		
		response.setResult(allQuestions);
		response.setSuccess(true);
		return response;
	}

	@Override
	public BaseResponseModel<QuestionDTO> getQuestionById(String id) {
		BaseResponseModel<QuestionDTO> response = new BaseResponseModel<>();
		QuestionDTO user = questionRepository.findById(id);
		
		if(user == null)
			return new BaseResponseModel<>(ErrorTypeEnum.NO_CONTENT.name(), "Question not founded bu given id id:".concat(id));
		
		response.setResult(user);
		response.setSuccess(true);
		return response;
	}

	@Override
	public BaseResponseModel<QuestionDTO> addOrUpdateQuestion(QuestionDTO question) {
		BaseResponseModel<QuestionDTO> response = new BaseResponseModel<>();
		
		try {
			if(question ==  null || StringUtils.isNullOrEmpty(question.getQuestion()))
				return new BaseResponseModel<>(ErrorTypeEnum.INVALID_REQUEST.name(), "Question cannot be empty!");
			
			if(CollectionUtils.isEmpty(question.getAnswers()))
				return new BaseResponseModel<>(ErrorTypeEnum.INVALID_REQUEST.name(), "Question answers cannot be empty!");

			if(question.getAnswers().size() != 2)
				return new BaseResponseModel<>(ErrorTypeEnum.INVALID_REQUEST.name(), "Question asnwers count must be 2!");
			
			if(question.getGroupId() < 1 || question.getGroupId() > 6)
				return new BaseResponseModel<>(ErrorTypeEnum.INVALID_REQUEST.name(), "Check group id - It must be between 1 or 6!");
			
			if(question.getDigit() < 1 || question.getDigit() > 4)
				return new BaseResponseModel<>(ErrorTypeEnum.INVALID_REQUEST.name(), "Check digit value - It must be betwwen 1 and 4!");
			
			for(QuestionAnswer answer : question.getAnswers()) {
				if(StringUtils.isNullOrEmpty(answer.getAnswer()) || answer.getValue() < 0 || answer.getValue() > 1)
					return new BaseResponseModel<>(ErrorTypeEnum.INVALID_REQUEST.name(), "Question asnwer error - check value or answer!");
			}

			QuestionDTO updatedQuestion = questionRepository.updateQuestion(question);

			if(updatedQuestion == null || StringUtils.isNullOrEmpty(updatedQuestion.getId()))
				return new BaseResponseModel<>(ErrorTypeEnum.DB_ERROR.name(), "Question cannot be updated!");
			
			response.setResult(updatedQuestion);
			response.setSuccess(true);
			return response;
		}catch(Exception e) {
			logger.error("::addOrUpdateQuestion exception question:{}", gson.toJson(question), e);
			return new BaseResponseModel<>(ErrorTypeEnum.EXCEPTION.name(), "Exception on addOrUpdateQuestion!");
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BaseResponseModel<Object> deleteQuestion(String id) {
		if(StringUtils.isNullOrEmpty(id))
			return new BaseResponseModel<>(ErrorTypeEnum.INVALID_REQUEST.name(), "Question id empty!");
			
		BaseResponseModel<QuestionDTO> questionDto = getQuestionById(id);
		
		if(questionDto == null || questionDto.getResult() == null)
			return new BaseResponseModel<>(ErrorTypeEnum.DB_ERROR.name(), "Question not founded!");
		
		QuestionDTO questionRes = questionDto.getResult();
		questionRes.setStatus(StatusEnum.DELETED.name());
		questionRepository.updateQuestion(questionRes);
		
		return new BaseResponseModel<>(true);
	}
}
