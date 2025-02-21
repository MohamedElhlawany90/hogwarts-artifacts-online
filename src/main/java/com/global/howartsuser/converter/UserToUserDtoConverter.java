package com.global.howartsuser.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.global.howartsuser.dto.HogwartsUser;
import com.global.howartsuser.dto.UserDto;

@Component
public class UserToUserDtoConverter implements Converter<HogwartsUser, UserDto>{

	@Override
	public UserDto convert(HogwartsUser source) {
		
		// We are not setting password in DTO.
		final UserDto userDto = new UserDto(source.getId(),
				                            source.getUsername(),
				                            source.isEnabled(),
                                            source.getRoles());
		
               return userDto;
	}

}
