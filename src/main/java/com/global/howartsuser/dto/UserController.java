package com.global.howartsuser.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.global.artifact.Artifact;
import com.global.artifact.dto.ArtifactDto;
import com.global.howartsuser.converter.UserDtoToUserConverter;
import com.global.howartsuser.converter.UserToUserDtoConverter;
import com.global.system.Result;
import com.global.system.StatusCode;
import com.global.wizard.Wizard;
import com.global.wizard.dto.WizardDto;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	
	
		private final UserService userService;
		
        private final UserDtoToUserConverter userDtoToUserConverter ;
		
		private final UserToUserDtoConverter userToUserDtoConverter ;   
		
		public UserController(UserService userService, UserDtoToUserConverter userDtoToUserConverter,UserToUserDtoConverter userToUserDtoConverter) {
			this.userService = userService;
			this.userDtoToUserConverter = userDtoToUserConverter;
			this.userToUserDtoConverter = userToUserDtoConverter;
		}
		

		@GetMapping
		public Result findAllUsers() {
	       List<HogwartsUser> foundHogwartsUsers = this.userService.findAll();
			
			// convert foundusers to list of usersDtos
			
			List<UserDto> userDtos = foundHogwartsUsers.stream()
			        .map(this.userToUserDtoConverter :: convert) 
			                .collect(Collectors.toList());
			
			return new Result (true, StatusCode.SUCCESS,"Find All Success",userDtos) ;
      }
		
		@GetMapping("/{userId}")
		public Result findUserById(@PathVariable Integer userId) {
			
		HogwartsUser foundHogwartsUser = this.userService.findById(userId);
		UserDto userDto = this.userToUserDtoConverter.convert(foundHogwartsUser);
		
			return new Result(true, StatusCode.SUCCESS, "Find One Success", userDto);
		
      }
		

		/*
	     * We are not using this update to change user password
	     * 
	     * @param user
	     * @retrun
	     */
		
		@PostMapping
		public Result addUser(@Valid @RequestBody HogwartsUser newHogwartsUser) { 
			HogwartsUser saveUser = this.userService.save(newHogwartsUser);
			UserDto savedUserDto =  this.userToUserDtoConverter.convert(saveUser);		    
			
			return new Result (true, StatusCode.SUCCESS,"Add Success",savedUserDto) ;
		}
		
		// We are not using this to update password, need another changePassword method in this class
		
		@PutMapping("/{userId}")
		public Result updateUser(@PathVariable Integer userId,@RequestBody @Valid UserDto userDto) {
			
			HogwartsUser update = this.userDtoToUserConverter.convert(userDto);
			HogwartsUser updatedHogwartsUser = this.userService.update(userId, update);
			UserDto updatedUserDto = this.userToUserDtoConverter.convert(updatedHogwartsUser);
			
			return new Result(true, StatusCode.SUCCESS, "Update Success", updatedUserDto) ;
		}
		
		
		@DeleteMapping("/{userId}")
		public Result deleteUser(@PathVariable Integer userId) {
			
			this.userService.delete(userId);
			return new Result(true,StatusCode.SUCCESS, "Delete Success") ;
			
		}
		
		
}
