package com.global.security;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.global.howartsuser.MyUserPrincipal;
import com.global.howartsuser.converter.UserToUserDtoConverter;
import com.global.howartsuser.dto.HogwartsUser;
import com.global.howartsuser.dto.UserDto;

@Service
public class AuthService {
	
	private final JwtProvider jwtProvider;
	
	private final UserToUserDtoConverter userToUserDtoConverter;

	public AuthService(JwtProvider jwtProvider, UserToUserDtoConverter userToUserDtoConverter) {
		super();
		this.jwtProvider = jwtProvider;
		this.userToUserDtoConverter = userToUserDtoConverter;
	}

	public Map<String, Object> createLoginInfo(Authentication authentication) {
		// Create user info.
		MyUserPrincipal principal = (MyUserPrincipal)authentication.getPrincipal();
		HogwartsUser hogwartsUser = principal.getHogwartsUser();
		UserDto userDto = this.userToUserDtoConverter.convert(hogwartsUser);
		
		// Create a JWT.
		String token = this.jwtProvider.createToken(authentication);
		
		Map<String, Object>loginResulrMap = new HashMap<>();
		
		loginResulrMap.put("userInfo", userDto);
		loginResulrMap.put("token", token);
		return loginResulrMap;
	}

}
