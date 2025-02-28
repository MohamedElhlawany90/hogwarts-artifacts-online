package com.global.howartsuser;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import com.global.howartsuser.dto.HogwartsUser;

public class MyUserPrincipal implements UserDetails{

	private HogwartsUser hogwartsUser ;
	
	public MyUserPrincipal(HogwartsUser hogwartsUser) {
		super();
		this.hogwartsUser = hogwartsUser;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// Convert a user's role from space-delimited to a list of SimpleGrantedAuthority objects.
		// for ex: john's role are stored in a string like "admin user modirator", we need to convert it to a list of GrantedAutority.
		//Before conversion, we need to add this "ROLE_" perfix to each role name.
		return Arrays.stream(StringUtils.tokenizeToStringArray(this.hogwartsUser.getRoles()," "))
				.map(role-> new SimpleGrantedAuthority("ROLE_" + role))
				.toList();
	}

	@Override
	public String getPassword() {
		
		return this.hogwartsUser.getPassword();
	}

	@Override
	public String getUsername() {
		return this.hogwartsUser.getUsername();
	}
	
	@Override
	public boolean isEnabled() {
		return this.hogwartsUser.isEnabled();
	}
	
	public HogwartsUser getHogwartsUser() {
		
		return hogwartsUser ;
	}

}
