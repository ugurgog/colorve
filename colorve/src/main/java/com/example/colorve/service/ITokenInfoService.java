package com.example.colorve.service;

import java.util.List;

import com.example.colorve.model.BaseResponseModel;
import com.example.colorve.model.db.TokenInfoDTO;

public interface ITokenInfoService {

	BaseResponseModel<List<TokenInfoDTO>> listAllTokens();
	
	BaseResponseModel<TokenInfoDTO> getTokenById(String id);
	
	BaseResponseModel<TokenInfoDTO> getTokenBySite(String site);
}
