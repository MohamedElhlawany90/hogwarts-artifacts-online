package com.global.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.global.system.Result;
import com.global.system.StatusCode;

@RestController
@RequestMapping("${api.endpoint.base-url}/users")
public class AuthController {
	
	private final AuthService authService;

	public static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
	
	public AuthController(AuthService authService) {
		super();
		this.authService = authService;
	}

	@PostMapping("/login")
	public Result getLoginInfo(Authentication authentication) {
		
		LOGGER.debug("Authenticated user: '{}'",authentication.getName());
		return new Result(true, StatusCode.SUCCESS, "User Info and JSON Web Token", this.authService.createLoginInfo(authentication));
		
	}
}
