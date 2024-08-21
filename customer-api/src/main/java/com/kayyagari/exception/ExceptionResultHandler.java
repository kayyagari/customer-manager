package com.kayyagari.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
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
}