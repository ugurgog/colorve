package com.example.colorve.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.example.colorve.enums.ErrorTypeEnum;
import com.example.colorve.model.AuthModel;
import com.example.colorve.model.BaseResponseModel;
import com.example.colorve.model.db.TokenInfoDTO;
import com.example.colorve.service.ITokenInfoService;
import com.example.colorve.util.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@Component
public class ContentWrapperFilter extends GenericFilterBean {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@Autowired
    private ITokenInfoService tokenService;
	
	@Autowired
	private Gson gson;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

		MultiReadHttpServletRequest requestWrapper = new MultiReadHttpServletRequest(
				(HttpServletRequest) servletRequest);

		try {
//			String authToken = requestWrapper.getHeader("AUTH-TOKEN");
//			String site = requestWrapper.getHeader("SITE");
			String authData = requestWrapper.getHeader("AUTH-DATA");
			AuthModel authModel = gson.fromJson(authData, AuthModel.class);
			
			logger.info("::doFilter authModel:{}", gson.toJson(authModel));
			
			if(authModel == null || StringUtils.isNullOrEmpty(authModel.getAuthToken()) || StringUtils.isNullOrEmpty(authModel.getSite())) {
				BaseResponseModel<Integer> response = new BaseResponseModel<>(ErrorTypeEnum.INVALID_REQUEST.name(), "AUTH-TOKEN or SITE empty in header!");
    			
    			String errorResponseBody = objectMapper.writeValueAsString(response);
    			HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
    			httpServletResponse.setStatus(500);
    			httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
    			httpServletResponse.getWriter().write(errorResponseBody);
    			httpServletResponse.getWriter().flush();
    			return;
			}

			BaseResponseModel<TokenInfoDTO> tokenModel = tokenService.getTokenBySite(authModel.getSite());
			
			logger.info("::doFilter tokenModel:{}", gson.toJson(tokenModel));
        	
			if(tokenModel == null || tokenModel.getResult() == null) {
				BaseResponseModel<Integer> response = new BaseResponseModel<>(ErrorTypeEnum.INVALID_REQUEST.name(), "SITE param in header undefined!");
    			
    			String errorResponseBody = objectMapper.writeValueAsString(response);
    			HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
    			httpServletResponse.setStatus(500);
    			httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
    			httpServletResponse.getWriter().write(errorResponseBody);
    			httpServletResponse.getWriter().flush();
    			return;
			}
			
			if(!authModel.getAuthToken().equals(tokenModel.getResult().getToken())) {
				BaseResponseModel<Integer> response = new BaseResponseModel<>(ErrorTypeEnum.INVALID_REQUEST.name(), "AUTH-TOKEN undefined!");
    			
    			String errorResponseBody = objectMapper.writeValueAsString(response);
    			HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
    			httpServletResponse.setStatus(500);
    			httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
    			httpServletResponse.getWriter().write(errorResponseBody);
    			httpServletResponse.getWriter().flush();
    			return;
			}
        	
        } catch (Exception e) {
            BaseResponseModel<Integer> response = new BaseResponseModel<>(ErrorTypeEnum.EXCEPTION.name(), "doFilter exception");
			
			String errorResponseBody = objectMapper.writeValueAsString(response);
			HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
			httpServletResponse.setStatus(500);
			httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
			httpServletResponse.getWriter().write(errorResponseBody);
			httpServletResponse.getWriter().flush();
            return;
        }
		filterChain.doFilter(requestWrapper, servletResponse);
    }

}