package com.example.colorve.model;

import java.io.Serializable;

public class BaseResponseModel<T> implements Serializable {

    private static final long serialVersionUID = 6073516571995341302L;
    private boolean success;
    private String errorCode;
    private String errorMessage;
    private T result;
    
    public BaseResponseModel() {
		super();
	}

	public BaseResponseModel(String errorCode, String errorMessage) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	
	public BaseResponseModel(String errorCode, String errorMessage, T result) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.result = result;
	}
	
	public BaseResponseModel(boolean success) {
		super();
		this.success = success;
	}

	public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}