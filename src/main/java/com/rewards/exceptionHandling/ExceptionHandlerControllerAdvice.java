package com.rewards.exceptionHandling;

import java.io.IOException;
import java.net.InetAddress;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import com.rewards.DTO.ErrorResponseDto;
import com.rewards.utils.ErrorMessage;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ExceptionHandlerControllerAdvice {
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody ErrorResponseDto handleException(final Exception exception, HttpServletRequest request)
			throws IOException {

		exception.printStackTrace();

		ErrorResponseDto error = new ErrorResponseDto();
		error.setMessage(ErrorMessage.INTERNAL_SERVER_EXCEPTION);

		return error;
	}

	@ExceptionHandler(NullPointerException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponseDto handleNullPointerException(NullPointerException ex, HttpServletRequest request) {
		ex.printStackTrace();
		ErrorResponseDto error = new ErrorResponseDto();
		error.setMessage(ErrorMessage.NULL_POINTER_EXCEPTION);
		return error;
	}


     @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


	
}
