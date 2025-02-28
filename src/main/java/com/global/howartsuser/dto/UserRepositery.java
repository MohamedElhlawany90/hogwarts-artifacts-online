package com.global.howartsuser.dto;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositery extends JpaRepository<HogwartsUser, Integer>{
	

	   Optional<HogwartsUser> findByUsername (String username);
		
}
