package com.example.colorve.service;

import java.util.List;

import com.example.colorve.model.BaseResponseModel;
import com.example.colorve.model.LocationModel;
import com.example.colorve.model.db.UserDTO;

public interface IUserService {

	BaseResponseModel<List<UserDTO>> listAllUsers();
	
	BaseResponseModel<List<UserDTO>> listAllUsersByStatus(String status);
	
	BaseResponseModel<UserDTO> getUserById(String id);
	
	BaseResponseModel<UserDTO> addUser(UserDTO user);
	
	BaseResponseModel<UserDTO> updateUser(UserDTO user);
	
	@SuppressWarnings("rawtypes")
	BaseResponseModel login(UserDTO user);
	
	@SuppressWarnings("rawtypes")
	BaseResponseModel deleteUser(String id);
	
	@SuppressWarnings("rawtypes")
	BaseResponseModel refreshLocation(String userId, LocationModel locationModel);
}
