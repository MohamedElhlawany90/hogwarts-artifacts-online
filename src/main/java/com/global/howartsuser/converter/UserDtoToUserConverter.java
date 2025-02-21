package com.global.howartsuser.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.global.howartsuser.dto.HogwartsUser;
import com.global.howartsuser.dto.UserDto;

@Component
public class UserDtoToUserConverter implements Converter<UserDto, HogwartsUser>{

	@Override
	public HogwartsUser convert(UserDto source) {


		HogwartsUser userHogwartsUser = new HogwartsUser();
		     userHogwartsUser.setUsername(source.username());
		     userHogwartsUser.setEnabled(source.enabled());
		     userHogwartsUser.setRoles(source.roles());
		     
		return userHogwartsUser;
	}

}
