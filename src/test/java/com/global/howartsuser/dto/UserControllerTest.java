package com.global.howartsuser.dto;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.global.system.StatusCode;
import com.global.system.Exeption.ObjectNotFoundException;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Turn off Spring Security because this is JUST API end point and we just need to test it we don't need security.
class UserControllerTest {

	
	@Autowired
	MockMvc mockMvc ;
	
	@MockitoBean
	UserService  userService ;
	
	@Autowired
	ObjectMapper objectMapper;
	
	List<HogwartsUser> users;
	
	@Value("/api/v1")
	String baseUrl ;
	
	
	@BeforeEach
	void setUp() throws Exception {
		this.users = new ArrayList<>();
		
		HogwartsUser u1 = new HogwartsUser();
		u1.setId(1);
		u1.setUsername("john");
		u1.setPassword("123456");
		u1.setEnabled(true);
		u1.setRoles("admin user");
		this.users.add(u1);
		
		HogwartsUser u2 = new HogwartsUser();
		u2.setId(2);
		u2.setUsername("eric");
		u2.setPassword("654321");
		u2.setEnabled(true);
		u2.setRoles("user");
		this.users.add(u2);
		
		HogwartsUser u3 = new HogwartsUser();
		u3.setId(3);
		u3.setUsername("tom");
		u3.setPassword("qwerty");
		u3.setEnabled(false);
		u3.setRoles("user");
		this.users.add(u3);
		
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	
	@Test
	   void testFindAllUserssSuccess() throws Exception {
		    
		        // Given
				 given(this.userService.findAll()).willReturn(this.users);
				
				// When And Then
				this.mockMvc.perform(get(this.baseUrl +"/users").accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.flag").value(true))
				.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
				.andExpect(jsonPath("$.message").value("Find All Success"))
				.andExpect(jsonPath("$.data", Matchers.hasSize(this.users.size())))
				.andExpect(jsonPath("$.data[0].id").value(1))
				.andExpect(jsonPath("$.data[0].username").value("john"))
				.andExpect(jsonPath("$.data[1].id").value(2))
				.andExpect(jsonPath("$.data[1].username").value("eric"));   
	   }
	@Test
	void testFindUserByIdSuccess() throws Exception {
		//Given
		
		given(this.userService.findById(1)).willReturn(this.users.get(0));
		
		//When & Then
		this.mockMvc.perform(get(this.baseUrl +"/users/1").accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.flag").value(true))
		.andExpect(jsonPath( "$.code").value(StatusCode.SUCCESS))
		.andExpect(jsonPath( "$.message").value("Find One Success"))
		.andExpect(jsonPath( "$.data.id").value(1))
		.andExpect(jsonPath( "$.data.username").value("john")) ;
		
	}
	
	@Test
	void testFindWizardByIdNotFound() throws Exception  {
		
		//Given
		given(this.userService.findById(5))
		.willThrow(new ObjectNotFoundException("user", 5));
		
		//When and Then
		this.mockMvc.perform(get(this.baseUrl +"/users/5").accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath( "$.flag").value(false))
		.andExpect(jsonPath( "$.code").value(StatusCode.NOT_FOUND))
		.andExpect(jsonPath( "$.message").value("Could not find user with Id 5 :("))
		.andExpect(jsonPath( "$.data").isEmpty()); 
		
	}
	
	 @Test
		void testAddUserSuccess() throws Exception {
			
			// Given
		 HogwartsUser user = new HogwartsUser();
		   user.setId(4);
		   user.setUsername("lily");
		   user.setPassword("123456");
		   user.setEnabled(true);
		   user.setRoles("admin user"); // The delimiter is space
			
			String json =  this.objectMapper.writeValueAsString(user);
			 user.setId(4);
			
			 
			 given(this.userService.save(Mockito.any(HogwartsUser.class))).willReturn(user);
			
			
			//When and Then
			 
			 this.mockMvc.perform(post(this.baseUrl +"/users").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.flag").value(true))
				.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
				.andExpect(jsonPath("$.message").value("Add Success"))
				.andExpect(jsonPath("$.data.id").isNotEmpty())
				.andExpect(jsonPath("$.data.username").value(user.getUsername()))
				.andExpect(jsonPath("$.data.enabled").value(user.isEnabled()))
				.andExpect(jsonPath("$.data.roles").value(user.getRoles()));
		}

	 @Test
		void testUpdateUserSuccesss() throws Exception {
			
			        // Given
					UserDto userDto  = new UserDto(  3
							                            ,"tom123"
							                            ,false
							                            ,"user" ) ;
					
					String json =  this.objectMapper.writeValueAsString(userDto);
					
					
					HogwartsUser updatedUser = new HogwartsUser();
					updatedUser.setId(3);
					updatedUser.setUsername("tom123");
					updatedUser.setEnabled(false);
					updatedUser.setRoles("user");
					 
					 given(this.userService.update(eq(3),Mockito.any(HogwartsUser.class))).willReturn(updatedUser);
					
					
					//When and Then
					 
					 this.mockMvc.perform(put(this.baseUrl +"/users/3").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
						.andExpect(jsonPath("$.flag").value(true))
						.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
						.andExpect(jsonPath("$.message").value("Update Success"))
						.andExpect(jsonPath("$.data.id").isNotEmpty())
						.andExpect(jsonPath("$.data.username").value(updatedUser.getUsername()))
						.andExpect(jsonPath("$.data.enabled").value(updatedUser.isEnabled()))
						.andExpect(jsonPath("$.data.roles").value(updatedUser.getRoles()));
		}
	 
	 @Test
		void testUpdateUserErrorWithNoExistenId() throws Exception {
			
			// Given
			
			 given(this.userService.update(eq(5),Mockito.any(HogwartsUser.class))).willThrow(new ObjectNotFoundException("user", 5));
			
			 UserDto userDto = new  UserDto(5,"tom123",false,"user");
			 
			String json =  this.objectMapper.writeValueAsString(userDto);

			//When and Then
			 
			 
			 this.mockMvc.perform(put(this.baseUrl +"/users/5").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.flag").value(false))
				.andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
				.andExpect(jsonPath("$.message").value("Could not find user with Id 5 :("))
				.andExpect(jsonPath("$.data").isEmpty());
		}
	 
	 @Test
		void testDeleteWizardSuccess() throws Exception {
			//Given
			
			doNothing().when(this.userService).delete(2);
			
			//When & Then
			this.mockMvc.perform(delete(this.baseUrl +"/users/3").accept(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.flag").value(true))
			.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
			.andExpect(jsonPath("$.message").value("Delete Success"));
			
		}
	 
	 @Test
		void testDeleteUserErrorWithNonExistenId() throws Exception {
			//Given
			
			doThrow(new ObjectNotFoundException("user", 5)).when(this.userService).delete(5);
			
			//When & Then
			this.mockMvc.perform(delete(this.baseUrl +"/users/5").accept(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.flag").value(false))
			.andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
			.andExpect(jsonPath("$.message").value("Could not find user with Id 5 :("))
			.andExpect(jsonPath("$.data").isEmpty());
			
		}
	 
}
