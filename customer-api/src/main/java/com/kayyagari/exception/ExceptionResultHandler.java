package com.kayyagari.exception;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * There are no custom exceptions, they are not created to keep this exercise simple.
 * 
 * This handler sets the ProblemDetail instance's detail to the Exception's message.  
 */
@ControllerAdvice
public class ExceptionResultHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ProblemDetail createProblemDetail(Exception ex, HttpStatusCode status, String defaultDetail,
			String detailMessageCode, Object[] detailMessageArguments, WebRequest request) {
		ProblemDetail pd = super.createProblemDetail(ex, status, defaultDetail, detailMessageCode, detailMessageArguments, request);
		if(ex.getMessage() != null) {
			pd.setDetail(ex.getMessage());
		}
		
		return pd;
	}

	// default implementation is not capturing the validation error message ProblemDetail
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		ResponseEntity<Object> resp = super.handleMethodArgumentNotValid(ex, headers, status, request);
		if(resp.getBody() instanceof ProblemDetail) {
			ProblemDetail pd = (ProblemDetail)resp.getBody();
			BindingResult br = ex.getBindingResult();
			if(br != null) {
				List<FieldError> feList = br.getFieldErrors();
				if(feList != null) {
					StringBuilder sb = new StringBuilder();
					for(FieldError fe : feList) {
						sb.append(fe.getDefaultMessage()).append('\n');
					}
					pd.setDetail(sb.toString());
				}
			}
		}
		
		return resp;
	}
}