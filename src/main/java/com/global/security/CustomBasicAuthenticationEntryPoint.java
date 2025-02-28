package com.global.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint{

	
	/*
	 * Here we have injected the DefaultHandlerExceptionResolver and delegated the handler to this resolve.
	 * This security exception can now be handled with controller advice with an exception handler method.
	 */
	
	private final HandlerExceptionResolver resolver;
	
	
	
	public CustomBasicAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
		
		this.resolver = resolver;
	}



	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,AuthenticationException authException) throws IOException, ServletException {
		
		response.addHeader("WWW-Athenticate", "Basic realm=\"Realm\"");
		this.resolver.resolveException(request, response, null, authException);
		
	}
	
	

}
