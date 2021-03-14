package com.deviget.challenge.minesweeper.api;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.deviget.challenge.minesweeper.core.exceptions.GameNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	static class ExceptionResponse {
		public long timestamp;
		public int status;
		public String error;
		public Object message;
		public String path;
	}

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
		log.error("Error on request "+ServletUriComponentsBuilder.fromCurrentRequest().build().toString(), ex);
		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.timestamp = System.currentTimeMillis();
		exceptionResponse.status = HttpStatus.INTERNAL_SERVER_ERROR.value();
		exceptionResponse.error = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
		if (Boolean.parseBoolean(request.getParameter("debug"))) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintWriter writer = new PrintWriter(baos);
			ex.printStackTrace(writer);
			writer.flush();
			exceptionResponse.message = baos.toString();
		} else {
			exceptionResponse.message = ex.getClass().getSimpleName();
		}
		exceptionResponse.path = ServletUriComponentsBuilder.fromCurrentRequest().build().toString();
		return handleExceptionInternal(ex, exceptionResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.timestamp = System.currentTimeMillis();
		exceptionResponse.status = HttpStatus.NOT_FOUND.value();
		exceptionResponse.error = HttpStatus.NOT_FOUND.getReasonPhrase();
		final List<Object> errorFields = new LinkedList<Object>();
		for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
			errorFields.add(new Object() {
				public String parameter = error.getField();
				public String message = error.getDefaultMessage();
				public Object value = error.getRejectedValue();
			});
			
		}
		exceptionResponse.message = new Object() {
			public String error = "Invalid parameters";
			public Object[] fields = errorFields.toArray();
		};
		exceptionResponse.path = ServletUriComponentsBuilder.fromCurrentRequest().build().toString();
		return handleExceptionInternal(ex, exceptionResponse, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.timestamp = System.currentTimeMillis();
		exceptionResponse.status = HttpStatus.NOT_FOUND.value();
		exceptionResponse.error = HttpStatus.NOT_FOUND.getReasonPhrase();
		exceptionResponse.message = new Object() {
			public String error = "Missing parameter";
			public String field = ex.getParameterName();
			public String type = ex.getParameterType();
		};
		exceptionResponse.path = ServletUriComponentsBuilder.fromCurrentRequest().build().toString();
		return handleExceptionInternal(ex, exceptionResponse, headers, status, request);
	}

	@ExceptionHandler(GameNotFoundException.class)
	public final ResponseEntity<Object> handleUserNotFoundException(GameNotFoundException ex, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.timestamp = System.currentTimeMillis();
		exceptionResponse.status = HttpStatus.NOT_FOUND.value();
		exceptionResponse.error = HttpStatus.NOT_FOUND.getReasonPhrase();
		exceptionResponse.message = new Object() {
			public String error = ex.getClass().getSimpleName();
			public String gameId = ex.getGameId();
		};
		exceptionResponse.path = ServletUriComponentsBuilder.fromCurrentRequest().build().toString();
		return handleExceptionInternal(ex, exceptionResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}

	
}
