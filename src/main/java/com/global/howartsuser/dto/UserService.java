package com.global.howartsuser.dto;

import java.util.List;

import org.springframework.stereotype.Service;

import com.global.artifact.Artifact;
import com.global.system.Exeption.ObjectNotFoundException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {
	
	private final UserRepositery userRepositery ;

	public UserService(UserRepositery userRepositery) {
		super();
		this.userRepositery = userRepositery;
	}
	
	

	public List<HogwartsUser> findAll() {
		
		return this.userRepositery.findAll();
	}
	
    public HogwartsUser findById(Integer userId) {
		
		return this.userRepositery.findById(userId)
				.orElseThrow(() -> new ObjectNotFoundException("user", userId)) ;
	}
    
    public HogwartsUser save(HogwartsUser newHogwartsUser) {
		
    	// /we NEEd to encode plain password before saving to the DB! TODO
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

	
}
