package com.global.howartsuser.dto;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.global.howartsuser.MyUserPrincipal;
import com.global.system.Exeption.ObjectNotFoundException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService implements UserDetailsService{
	
	private final UserRepositery userRepositery ;
	
	private final PasswordEncoder passwordEncoder ;

	public UserService(UserRepositery userRepositery, PasswordEncoder passwordEncoder) {
		
		this.userRepositery = userRepositery;
		this.passwordEncoder = passwordEncoder;
	}
	
	

	public List<HogwartsUser> findAll() {
		
		return this.userRepositery.findAll();
	}
	
    public HogwartsUser findById(Integer userId) {
		
		return this.userRepositery.findById(userId)
				.orElseThrow(() -> new ObjectNotFoundException("user", userId)) ;
	}
    
    public HogwartsUser save(HogwartsUser newHogwartsUser) {
		
    	// /we NEEd to encode plain text password before saving to the DB! TODO
    	newHogwartsUser.setPassword(this.passwordEncoder.encode(newHogwartsUser.getPassword()));
		return this.userRepositery.save(newHogwartsUser) ;
		
	}
    
    /*
     * We are not using this update to change user password
     * 
     * @param userId
     * @param update
     * @retrun
     */
	
	public HogwartsUser update(Integer userId, HogwartsUser update) {
		
		HogwartsUser oldHogwartsUser = this.userRepositery.findById(userId)
				   .orElseThrow(()-> new ObjectNotFoundException("user", userId));
		           oldHogwartsUser.setUsername(update.getUsername());
		           oldHogwartsUser.setEnabled(update.isEnabled());
		           oldHogwartsUser.setRoles(update.getRoles());
					
					return this.userRepositery.save(oldHogwartsUser);			
		
	}
	
	

	public void delete(Integer userId) {
		
		 this.userRepositery.findById(userId)
				.orElseThrow(()->new ObjectNotFoundException( "user", userId));
		
		this.userRepositery.deleteById(userId);
		
	}



	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	return this.userRepositery.findByUsername(username) // First, we need to find this user from database
		.map(hogwartsUser -> new MyUserPrincipal(hogwartsUser))// If found, wrap the returned user instance in a MyUserPrincipal instance.
		.orElseThrow(()-> new UsernameNotFoundException("username" + username + "is not found."));// Otherwise, throw an exception
		
	}

	
}
