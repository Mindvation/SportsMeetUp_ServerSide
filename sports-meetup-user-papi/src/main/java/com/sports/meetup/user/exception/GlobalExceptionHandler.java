package com.sports.meetup.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.sports.meetup.user.constant.ConstantFields;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(value = { BadRequestException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiDefaultResponse badRequestException(BadRequestException ex) {
        return new ApiDefaultResponse(ConstantFields.getBadRequestCode(), ex.getMessage(), null);
    }
    
    @ExceptionHandler(value = { IllegalArgumentException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiDefaultResponse illegalArgumentException(IllegalArgumentException ex) {
        return new ApiDefaultResponse("400", ex.getMessage(), null);
    }
    
    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiDefaultResponse methodArgumentNotValidException(MethodArgumentNotValidException ex) {
    	return new ApiDefaultResponse("400", ex.getMessage(), null);
    }
    
    @ExceptionHandler(value = { NoHandlerFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiDefaultResponse noHandlerFoundException(Exception ex) {
        return new ApiDefaultResponse("404", ex.getMessage(), null);
    }

    
    @ExceptionHandler(value = { LoginFaildException.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiDefaultResponse userNotExistException(LoginFaildException ex) {
    	return new ApiDefaultResponse("503", ex.getMessage(), null);
    }

    @ExceptionHandler(value = { Exception.class })
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiDefaultResponse unknownException(Exception ex) {
    	String message = ex.getMessage();
        return new ApiDefaultResponse("RUNTIME_500", "SAPI异常", null);
    }
}
