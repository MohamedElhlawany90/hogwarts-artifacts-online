package com.global.system.Exeption;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.global.artifact.ArtifactNotFoundExeption;
import com.global.system.Result;
import com.global.system.StatusCode;

@RestControllerAdvice
public class ExeptionHandlerAdvice {
	
	
	@ExceptionHandler(ArtifactNotFoundExeption.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	Result handleArtifactNotFoundExeption(ArtifactNotFoundExeption ex) {
		
		return new Result(false,StatusCode.NOT_FOUND , ex.getMessage()) ;
		  
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	Result handleValidationExeption(MethodArgumentNotValidException ex) {
		List<ObjectError> errors  = ex.getBindingResult().getAllErrors();
		Map<String , String> map = new HashMap<>(errors.size());
		errors.forEach((error) -> {
			String key = ((FieldError)error).getField();
			String val = error.getDefaultMessage();
			map.put(key, val);
			
		});
		return new Result (false, StatusCode.INVALID_ARGUMENT,"Provided arguments are invalid, see data for details.", map);
		
		
		
		
		  
	}

}
