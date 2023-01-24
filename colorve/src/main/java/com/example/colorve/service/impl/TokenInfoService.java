package com.example.colorve.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.example.colorve.enums.ErrorTypeEnum;
import com.example.colorve.model.BaseResponseModel;
import com.example.colorve.model.db.TokenInfoDTO;
import com.example.colorve.repository.TokenInfoRepository;
import com.example.colorve.service.ITokenInfoService;

@Service
public class TokenInfoService implements ITokenInfoService{

	@Autowired
	private TokenInfoRepository tokenRepository;
	
	@Override
	public BaseResponseModel<List<TokenInfoDTO>> listAllTokens() {
		BaseResponseModel<List<TokenInfoDTO>> response = new BaseResponseModel<>();
		List<TokenInfoDTO> allTokens = tokenRepository.findAll();
		
		if(CollectionUtils.isEmpty(allTokens))
			return new BaseResponseModel<>(ErrorTypeEnum.NO_CONTENT.name(), "No token founded!");
		
		response.setResult(allTokens);
		response.setSuccess(true);
		return response;
	}

	@Override
	public BaseResponseModel<TokenInfoDTO> getTokenById(String id) {
		BaseResponseModel<TokenInfoDTO> response = new BaseResponseModel<>();
		TokenInfoDTO token = tokenRepository.findById(id);
		
		if(token == null)
			return new BaseResponseModel<>(ErrorTypeEnum.NO_CONTENT.name(), "Token not founded bu given id id:".concat(id));
		
		response.setResult(token);
		response.setSuccess(true);
		return response;
	}

	@Override
	public BaseResponseModel<TokenInfoDTO> getTokenBySite(String site) {
		BaseResponseModel<TokenInfoDTO> response = new BaseResponseModel<>();
		TokenInfoDTO token = tokenRepository.findBySite(site);
		
		if(token == null)
			return new BaseResponseModel<>(ErrorTypeEnum.NO_CONTENT.name(), "Token not founded bu given site site:".concat(site));
		
		response.setResult(token);
		response.setSuccess(true);
		return response;
	}
}
