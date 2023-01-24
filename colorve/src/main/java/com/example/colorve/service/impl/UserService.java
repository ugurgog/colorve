package com.example.colorve.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.example.colorve.enums.ErrorTypeEnum;
import com.example.colorve.enums.StatusEnum;
import com.example.colorve.model.BaseResponseModel;
import com.example.colorve.model.LocationModel;
import com.example.colorve.model.db.UserDTO;
import com.example.colorve.repository.UserRepository;
import com.example.colorve.service.IUserService;
import com.example.colorve.util.PasswordUtils;
import com.example.colorve.util.StringUtils;
import com.google.gson.Gson;

@Service
public class UserService implements IUserService{

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private Gson gson;
	
	@Override
	public BaseResponseModel<List<UserDTO>> listAllUsers() {
		BaseResponseModel<List<UserDTO>> response = new BaseResponseModel<>();
		List<UserDTO> allUsers = userRepository.findAll();
		
		if(CollectionUtils.isEmpty(allUsers))
			return new BaseResponseModel<>(ErrorTypeEnum.NO_CONTENT.name(), "No user founded!");
		
		response.setResult(allUsers);
		response.setSuccess(true);
		return response;
	}
	
	@Override
	public BaseResponseModel<List<UserDTO>> listAllUsersByStatus(String status) {
		BaseResponseModel<List<UserDTO>> response = new BaseResponseModel<>();
		List<UserDTO> allUsers = userRepository.findAllByStatus(status);
		
		if(CollectionUtils.isEmpty(allUsers))
			return new BaseResponseModel<>(ErrorTypeEnum.NO_CONTENT.name(), "No user founded!");
		
		response.setResult(allUsers);
		response.setSuccess(true);
		return response;
	}

	@Override
	public BaseResponseModel<UserDTO> getUserById(String id) {
		BaseResponseModel<UserDTO> response = new BaseResponseModel<>();
		UserDTO user = userRepository.findById(id);
		
		if(user == null)
			return new BaseResponseModel<>(ErrorTypeEnum.NO_CONTENT.name(), "User not founded bu given id id:".concat(id));
		
		response.setResult(user);
		response.setSuccess(true);
		return response;
	}
	
	@Override
	public BaseResponseModel<UserDTO> addUser(UserDTO user) {
		BaseResponseModel<UserDTO> response = new BaseResponseModel<>();
		
		try {
			if(StringUtils.isNullOrEmpty(user.getEmail()))
				return new BaseResponseModel<>(ErrorTypeEnum.ERROR.name(), "Email cannot be empty!");
			
			UserDTO userDto = userRepository.findByEmail(user.getEmail());
			
			if(userDto != null)
				return new BaseResponseModel<>(ErrorTypeEnum.ERROR.name(), "Email is used by other user!");
			
			if(!StringUtils.isNullOrEmpty(user.getPassword()))
				user.setPassword(PasswordUtils.generateSha256Hash(user.getPassword()));
			
			UserDTO updatedUser = userRepository.updateUser(user);

			if(updatedUser == null || StringUtils.isNullOrEmpty(updatedUser.getId()))
				return new BaseResponseModel<>(ErrorTypeEnum.DB_ERROR.name(), "User cannot be added!");
			
			response.setResult(updatedUser);
			response.setSuccess(true);
			return response;
		}catch(Exception e) {
			logger.error("::updateUser exception user:{}", gson.toJson(user), e);
			return new BaseResponseModel<>(ErrorTypeEnum.EXCEPTION.name(), "Exception on updateUser!");
		}
	}

	@Override
	public BaseResponseModel<UserDTO> updateUser(UserDTO user) {
		BaseResponseModel<UserDTO> response = new BaseResponseModel<>();
		
		try {
			if(StringUtils.isNullOrEmpty(user.getEmail()))
				return new BaseResponseModel<>(ErrorTypeEnum.ERROR.name(), "Email cannot be empty!");
			
			UserDTO userDto = userRepository.findByEmail(user.getEmail());
			
			if(userDto != null && !user.getId().equals(userDto.getId()))
				return new BaseResponseModel<>(ErrorTypeEnum.ERROR.name(), "Email is used by other user!");
			
			UserDTO updatedUser = userRepository.updateUser(user);

			if(updatedUser == null || StringUtils.isNullOrEmpty(updatedUser.getId()))
				return new BaseResponseModel<>(ErrorTypeEnum.DB_ERROR.name(), "User cannot be updated!");
			
			response.setResult(updatedUser);
			response.setSuccess(true);
			return response;
		}catch(Exception e) {
			logger.error("::updateUser exception user:{}", gson.toJson(user), e);
			return new BaseResponseModel<>(ErrorTypeEnum.EXCEPTION.name(), "Exception on updateUser!");
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BaseResponseModel deleteUser(String id) {
		if(StringUtils.isNullOrEmpty(id))
			return new BaseResponseModel<>(ErrorTypeEnum.INVALID_REQUEST.name(), "User id empty!");
			
		BaseResponseModel<UserDTO> updatedUser = getUserById(id);
		
		if(updatedUser == null || updatedUser.getResult() == null)
			return new BaseResponseModel<>(ErrorTypeEnum.DB_ERROR.name(), "User not founded!");
		
		UserDTO user = updatedUser.getResult();
		user.setStatus(StatusEnum.DELETED.name());
		userRepository.updateUser(user);
		
		return new BaseResponseModel<>(true);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BaseResponseModel login(UserDTO user) {
		try {
			if(user == null || StringUtils.isNullOrEmpty(user.getEmail()) || StringUtils.isNullOrEmpty(user.getPassword()))
				return new BaseResponseModel<>(ErrorTypeEnum.INVALID_REQUEST.name(), "Email or password cannot be empty!");
			
			UserDTO userDto = userRepository.findByEmail(user.getEmail());
			
			if(userDto == null)
				return new BaseResponseModel<>(ErrorTypeEnum.INVALID_REQUEST.name(), "No user founded by the given email!");
			
			if(!PasswordUtils.checkPassword(user.getPassword(), userDto.getPassword()))
				return new BaseResponseModel<>(ErrorTypeEnum.ERROR.name(), "Password wrong!");
			
			return new BaseResponseModel<>(true);
		}catch(Exception e) {
			logger.error("::login exception user:{}", gson.toJson(user), e);
			return new BaseResponseModel<>(ErrorTypeEnum.EXCEPTION.name(), "Exception on login!");
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public BaseResponseModel refreshLocation(String userId, LocationModel locationModel) {
		try {
			if(locationModel == null || StringUtils.isNullOrEmpty(userId))
				return new BaseResponseModel<>(ErrorTypeEnum.INVALID_REQUEST.name(), "Lokasyon veya kullanıcı id geçersiz!");
			
			if(locationModel.getLatitude() <= 0 || locationModel.getLongitude() <= 0)
				return new BaseResponseModel<>(ErrorTypeEnum.INVALID_REQUEST.name(), "Lokasyon bilgisi hatalı geçersiz!");
			
			UserDTO userDto = userRepository.findById(userId);
			
			if(userDto == null)
				return new BaseResponseModel<>(ErrorTypeEnum.ERROR.name(), "Kullanıcı bulunamadı!");
			
			userDto.setLocation(locationModel);
			
			UserDTO updatedUser = userRepository.updateUser(userDto);
			
			if(updatedUser == null)
				return new BaseResponseModel<>(ErrorTypeEnum.ERROR.name(), "Kullanıcı lokasyon bilgisi güncellenemedi!");
			
			return new BaseResponseModel<>(true);
		}catch(Exception e) {
			logger.error("::refreshLocation exception locationModel:{}", gson.toJson(locationModel), e);
			return new BaseResponseModel<>(ErrorTypeEnum.EXCEPTION.name(), "Exception on refreshLocation!");
		}
	}
}
