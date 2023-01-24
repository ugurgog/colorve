package com.example.colorve.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.colorve.enums.ErrorTypeEnum;
import com.example.colorve.model.BaseResponseModel;
import com.example.colorve.model.LocationModel;
import com.example.colorve.model.db.UserDTO;
import com.example.colorve.service.IUserService;
import com.example.colorve.service.jwt.JWTService;
import com.example.colorve.util.StringUtils;
import com.google.gson.Gson;

@RestController
@RequestMapping("/user")
public class UserController {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
    private IUserService userService;
	
	@Autowired
    private JWTService jwtService;
	
	@Autowired
	private Gson gson;
    
	@GetMapping(path = "/list-all")
    public BaseResponseModel<List<UserDTO>> listAllUsers() {
        try {
            return userService.listAllUsers();
        }catch (Exception e){
            return new BaseResponseModel<>(ErrorTypeEnum.EXCEPTION.name(), e.getLocalizedMessage());
        }
    }
	
	@GetMapping(path = "/list-all-by-status/{status}")
    public BaseResponseModel<List<UserDTO>> listAllUsersByStatus(@PathVariable("status") String status) {
        try {
            return userService.listAllUsersByStatus(status);
        }catch (Exception e){
            return new BaseResponseModel<>(ErrorTypeEnum.EXCEPTION.name(), e.getLocalizedMessage());
        }
    }
	
	@GetMapping(path = "/get-user/{userId}")
    public BaseResponseModel<UserDTO> getUser(@PathVariable("userId") String userId) {
        try {
            return userService.getUserById(userId);
        }catch (Exception e){
            return new BaseResponseModel<>(ErrorTypeEnum.EXCEPTION.name(), e.getLocalizedMessage());
        }
    }
	
	@PostMapping(path = "/add-user" , consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponseModel<UserDTO> addUser(@RequestBody UserDTO request) {
        try {
        	if(request == null || !StringUtils.isNullOrEmpty(request.getId()))
        		return new BaseResponseModel<>(ErrorTypeEnum.INVALID_REQUEST.name(), "Id must be empty. For update, call /update-user");
        	
            return userService.addUser(request);
        }catch (Exception e){
            return new BaseResponseModel<>(ErrorTypeEnum.EXCEPTION.name(), e.getLocalizedMessage());
        }
    }
	
	@SuppressWarnings("rawtypes")
	@PostMapping(path = "/refresh-location/{userId}" , consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponseModel refreshLocation(@PathVariable("userId") String userId, @RequestBody LocationModel request) {
        try {
            return userService.refreshLocation(userId, request);
        }catch (Exception e){
            return new BaseResponseModel<>(ErrorTypeEnum.EXCEPTION.name(), e.getLocalizedMessage());
        }
    }
	
	@PostMapping(path = "/update-user" , consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponseModel<UserDTO> updateUser(@RequestBody UserDTO userDto) {
        try {
        	if(userDto == null || StringUtils.isNullOrEmpty(userDto.getId()))
        		return new BaseResponseModel<>(ErrorTypeEnum.INVALID_REQUEST.name(), "Id must not be empty. For add, call /add-user");
        	
            return userService.updateUser(userDto);
        }catch (Exception e){
            return new BaseResponseModel<>(ErrorTypeEnum.EXCEPTION.name(), e.getLocalizedMessage());
        }
    }
	
	@SuppressWarnings("rawtypes")
	@PostMapping(path = "/login" , consumes = MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponseModel login(@RequestBody UserDTO userDto, HttpServletResponse response) {
        try {
        	BaseResponseModel resp = userService.login(userDto);
        	
        	if (resp != null && resp.isSuccess()) {
                String token = jwtService.createToken(userDto.getId());
                response.addHeader("Authorization", "Bearer " + token);
                return resp;
            } else {
            	return new BaseResponseModel<>(ErrorTypeEnum.ERROR.name(), "Jwt token not created!");
            }
        }catch (Exception e){
        	logger.error("::login exception userDto:{}", gson.toJson(userDto), e);
            return new BaseResponseModel<>(ErrorTypeEnum.EXCEPTION.name(), e.getLocalizedMessage());
        }
    }
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping(path = "/delete-user/{userId}")
    public BaseResponseModel deleteUser(@PathVariable("userId") String userId) {
        try {
            return userService.deleteUser(userId);
        }catch (Exception e){
            return new BaseResponseModel<>(ErrorTypeEnum.EXCEPTION.name(), e.getLocalizedMessage());
        }
    }

}
