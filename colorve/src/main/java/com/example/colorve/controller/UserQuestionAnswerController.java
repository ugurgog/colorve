package com.example.colorve.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.colorve.enums.ErrorTypeEnum;
import com.example.colorve.model.BaseResponseModel;
import com.example.colorve.model.db.UserQuestionAnswersDTO;
import com.example.colorve.model.question.UserQuestionAnswerResponseModel;
import com.example.colorve.service.IUserQuestionAnswerService;

@RestController
@RequestMapping("/user-answer")
public class UserQuestionAnswerController {
	
	@Autowired
    private IUserQuestionAnswerService userQuestionAnswerService;
    
	@GetMapping(path = "/get-user-answer/{id}")
    public BaseResponseModel<UserQuestionAnswersDTO> getUserAnswer(@PathVariable("id") String id) {
        try {
            return userQuestionAnswerService.getUserQuestionAnswerById(id);
        }catch (Exception e){
            return new BaseResponseModel<>(ErrorTypeEnum.EXCEPTION.name(), e.getLocalizedMessage());
        }
    }
	
	@GetMapping(path = "/list-user-answers/{userId}")
    public BaseResponseModel<List<UserQuestionAnswersDTO>> findUserQuestionAnswersByUserId(@PathVariable("userId") String userId) {
        try {
            return userQuestionAnswerService.findUserQuestionAnswersByUserId(userId);
        }catch (Exception e){
            return new BaseResponseModel<>(ErrorTypeEnum.EXCEPTION.name(), e.getLocalizedMessage());
        }
    }
	
	@PostMapping(path = "/save-user-answer" , consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponseModel<UserQuestionAnswerResponseModel> saveUserAnswer(@RequestBody UserQuestionAnswersDTO request) {
        try {
            return userQuestionAnswerService.addOrUpdateUserQuestionAnswer(request);
        }catch (Exception e){
            return new BaseResponseModel<>(ErrorTypeEnum.EXCEPTION.name(), e.getLocalizedMessage());
        }
    }
}
