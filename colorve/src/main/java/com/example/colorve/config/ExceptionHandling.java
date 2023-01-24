package com.example.colorve.config;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.example.colorve.model.BaseResponseModel;
import com.mongodb.MongoWriteException;

@ControllerAdvice
public class ExceptionHandling implements ErrorController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String METHOD_IS_NOT_ALLOWED = "This request method is not allowed on this endpoint. Please send a '%s' request";
    private static final String INTERNAL_SERVER_ERROR_MSG = "An error occurred while processing the request";
    private static final String ERROR_PROCESSING_FILE = "Error occurred while processing file";
    private static final String NOT_ENOUGH_PERMISSION = "You do not have enough permission";
    public static final String ERROR_PATH = "/error";

    @SuppressWarnings("rawtypes")
	@ExceptionHandler(value = MissingServletRequestParameterException.class)
    public BaseResponseModel handleMissingParams(MissingServletRequestParameterException ex) {
        return createHttpResponse(BAD_REQUEST, String.format("Missing parameter with name:%s", ex.getParameterName()));
    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(value = AccessDeniedException.class)
    public BaseResponseModel accessDeniedException() {
        return createHttpResponse(FORBIDDEN, NOT_ENOUGH_PERMISSION);
    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(value = NoHandlerFoundException.class)
    public BaseResponseModel noHandlerFoundException(NoHandlerFoundException e) {
        return createHttpResponse(BAD_REQUEST, "There is no mapping for this URL");
    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public BaseResponseModel methodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        HttpMethod supportedMethod = Objects.requireNonNull(exception.getSupportedHttpMethods()).iterator().next();
        return createHttpResponse(METHOD_NOT_ALLOWED, String.format(METHOD_IS_NOT_ALLOWED, supportedMethod));
    }
    
    @SuppressWarnings("rawtypes")
    @ExceptionHandler(value = DuplicateKeyException.class)
    public BaseResponseModel duplicateKeyException(DuplicateKeyException exception) {
        logger.error(exception.getMessage());
        return createHttpResponse(INTERNAL_SERVER_ERROR, ERROR_PROCESSING_FILE);
    }
    
    @SuppressWarnings("rawtypes")
    @ExceptionHandler(value = MongoWriteException.class)
    public BaseResponseModel mongoWriteException(MongoWriteException exception) {
        logger.error(exception.getMessage());
        return createHttpResponse(INTERNAL_SERVER_ERROR, ERROR_PROCESSING_FILE);
    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(value = Exception.class)
    public BaseResponseModel internalServerErrorException(Exception exception) {
        logger.error(exception.getMessage());
        return createHttpResponse(INTERNAL_SERVER_ERROR, String.format(INTERNAL_SERVER_ERROR_MSG + ":%s", exception.getMessage()));
    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(value = IOException.class)
    public BaseResponseModel iOException(IOException exception) {
        logger.error(exception.getMessage());
        return createHttpResponse(INTERNAL_SERVER_ERROR, ERROR_PROCESSING_FILE);
    }

    @SuppressWarnings("rawtypes")
    private BaseResponseModel createHttpResponse(HttpStatus httpStatus, String message) {
        return new BaseResponseModel(httpStatus.name(), message);
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping(ERROR_PATH)
    public BaseResponseModel notFound404() {
        return createHttpResponse(NOT_FOUND, "There is no mapping for this URL");
    }

}