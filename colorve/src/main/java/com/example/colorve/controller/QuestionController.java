package com.example.colorve.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.colorve.enums.ErrorTypeEnum;
import com.example.colorve.model.BaseResponseModel;
import com.example.colorve.model.db.QuestionDTO;
import com.example.colorve.service.IQuestionService;

@RestController
@RequestMapping("/question")
public class QuestionController {
	
	@Autowired
    private IQuestionService questionService;
    
	@GetMapping(path = "/list-all")
    public BaseResponseModel<List<QuestionDTO>> listAllQuestions(@RequestParam(value = "status", required=false) String status,
    		@RequestParam(value = "groupId", required=false) Integer groupId,
    		@RequestParam(value = "digit", required=false) Integer digit) {
        try {
            return questionService.listAllQuestions(status, groupId, digit);
        }catch (Exception e){
            return new BaseResponseModel<>(ErrorTypeEnum.EXCEPTION.name(), e.getLocalizedMessage());
        }
    }
	
	@GetMapping(path = "/list-to-answer")
    public BaseResponseModel<List<QuestionDTO>> listQuestionsToAnswer() {
        try {
            return questionService.listQuestionsToAnswer();
        }catch (Exception e){
            return new BaseResponseModel<>(ErrorTypeEnum.EXCEPTION.name(), e.getLocalizedMessage());
        }
    }
	
	@GetMapping(path = "/get-question/{questionId}")
    public BaseResponseModel<QuestionDTO> getQuestion(@PathVariable("questionId") String questionId) {
        try {
            return questionService.getQuestionById(questionId);
        }catch (Exception e){
            return new BaseResponseModel<>(ErrorTypeEnum.EXCEPTION.name(), e.getLocalizedMessage());
        }
    }
	
	@PostMapping(path = "/add-or-update-question" , consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponseModel<QuestionDTO> addQuestion(@RequestBody QuestionDTO request) {
        try {
            return questionService.addOrUpdateQuestion(request);
        }catch (Exception e){
            return new BaseResponseModel<>(ErrorTypeEnum.EXCEPTION.name(), e.getLocalizedMessage());
        }
    }

	@SuppressWarnings("rawtypes")
	@DeleteMapping(path = "/delete-question/{questionId}")
    public BaseResponseModel deleteQuestion(@PathVariable("questionId") String questionId) {
        try {
            return questionService.deleteQuestion(questionId);
        }catch (Exception e){
            return new BaseResponseModel<>(ErrorTypeEnum.EXCEPTION.name(), e.getLocalizedMessage());
        }
    }

}
