package com.global.howartsuser.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.global.system.Exeption.ObjectNotFoundException;
import com.global.wizard.Wizard;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	
	@Mock
	UserRepositery userRepositery;
	
	@InjectMocks
	UserService userService;
	
	List<HogwartsUser> hogwartsUsers ;
	

	@BeforeEach
	void setUp() throws Exception {
		
		HogwartsUser u1 = new HogwartsUser();
		u1.setId(1);
		u1.setUsername("john");
		u1.setPassword("123456");
		u1.setEnabled(true);
		u1.setRoles("admin user");
		
		HogwartsUser u2 = new HogwartsUser();
		u2.setId(2);
		u2.setUsername("eric");
		u2.setPassword("654321");
		u2.setEnabled(true);
		u2.setRoles("user");
		
		HogwartsUser u3 = new HogwartsUser();
		u3.setId(3);
		u3.setUsername("tom");
		u3.setPassword("qwerty");
		u3.setEnabled(false);
		u3.setRoles("user");
		
		this.hogwartsUsers = new ArrayList<>();
		this.hogwartsUsers.add(u1);
		this.hogwartsUsers.add(u2);
		this.hogwartsUsers.add(u3);
		
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	
	@Test
	   void testFIndAllSuccess() {
		   // Given. Arrange inputs and targets. Define a behavior of Mock
		   given(this.userService.findAll()).willReturn(this.hogwartsUsers);
		   
		   // When. Act on the target behavior. Act steps should cover the method
		   List<HogwartsUser> actualUsers = this.userService.findAll();
		   
		   //Then. Assert expected outcomes
		   assertThat(actualUsers.size()).isEqualTo(this.hogwartsUsers.size());
		   
		   verify(userRepositery, times(1)).findAll();
		   
	   }
	
	@Test
	void testFindByIdSuccess() {
		
		//Given

		HogwartsUser u1 = new HogwartsUser();
		u1.setId(1);
		u1.setUsername("john");
		u1.setPassword("123456");
		u1.setEnabled(true);
		u1.setRoles("admin user");
		
		given(this.userRepositery.findById(1)).willReturn(Optional.of(u1));
		
		//When
		HogwartsUser returnedUser = this.userService.findById(1);
		
		//Then
		assertThat(returnedUser.getId()).isEqualTo(u1.getId());
		assertThat(returnedUser.getUsername()).isEqualTo(u1.getUsername());
		assertThat(returnedUser.getPassword()).isEqualTo(u1.getPassword());
		assertThat(returnedUser.isEnabled()).isEqualTo(u1.isEnabled());
		assertThat(returnedUser.getRoles()).isEqualTo(u1.getRoles());
		
		verify(this.userRepositery, times(1)).findById(1);
	}
	
	@Test
	void testFindByIdNotFound() {
		//Given
		given(this.userRepositery.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());
		
		//When
		Throwable thrown = catchThrowable(()->{
			   
			HogwartsUser returnedUser  = this.userService.findById(1);
	   });
		
		//Then
		
		 assertThat(thrown)
		   .isInstanceOf(ObjectNotFoundException.class)
		   .hasMessage("Could not find user with Id 1 :("); 
		   
			verify(this.userRepositery, times(1)).findById(1);
	}
	
	@Test
	   void testSaveSuccess() {
		   
		   // Given
		   HogwartsUser newUser = new HogwartsUser();
		   newUser.setUsername("lily");
		   newUser.setPassword("123456");
		   newUser.setEnabled(true);
		   newUser.setRoles("user");
		  
		   given(this.userRepositery.save(newUser)).willReturn(newUser);
		   
		   // When
		   HogwartsUser returnedUser = this.userService.save(newUser);
		   
		   // Then
		   assertThat(returnedUser.getUsername()).isEqualTo(newUser.getUsername());
		   assertThat(returnedUser.getPassword()).isEqualTo(newUser.getPassword());
		   assertThat(returnedUser.isEnabled()).isEqualTo(newUser.isEnabled());
		   assertThat(returnedUser.getRoles()).isEqualTo(newUser.getRoles());
		   
		   verify(this.userRepositery, times(1)).save(newUser);
		   
	   }
	
	 @Test
	   void testUpdateSuccess() {
		   
		   //Given
		   HogwartsUser oldUser = new HogwartsUser();
		   oldUser.setId(1);
		   oldUser.setUsername("john");
		   oldUser.setPassword("123456");
		   oldUser.setEnabled(true);
		   oldUser.setRoles("admin user");
		   
		   HogwartsUser update = new HogwartsUser();
		   update.setId(1);
		   update.setUsername("john - update");
		   update.setPassword("123456");
		   update.setEnabled(true);
		   update.setRoles("admin user"); 
		   
		   given(this.userRepositery.findById(1)).willReturn(Optional.of(oldUser));
		   given(this.userRepositery.save(oldUser)).willReturn(oldUser);
		   
		   // When
		   
		   HogwartsUser updatedUser = this.userService.update(1, update);
		   
		   //Then
		   
		    assertThat(updatedUser.getId()).isEqualTo(1);  
		    assertThat(updatedUser.getUsername()).isEqualTo(update.getUsername());
		   
		   verify(this.userRepositery, times(1)).findById(1);
		   verify(this.userRepositery, times(1)).save(oldUser);

	   }
	 
	 @Test
	   void testUpdateNotFound() {
		   
		   // Given
		   HogwartsUser update = new HogwartsUser();
		   update.setId(1);
		   update.setUsername("john - update");
		   update.setPassword("123456");
		   update.setEnabled(true);
		   update.setRoles("admin user");
		   
		   given(this.userRepositery.findById(1)).willReturn(Optional.empty());
		   
		   // When
		   
		Throwable thrown = assertThrows(ObjectNotFoundException.class, ()->{
			  
			  this.userService.update(1, update);
		  });
		   
		   // Then
		  assertThat(thrown)
		  .isInstanceOf(ObjectNotFoundException.class)
		  .hasMessage("Could not find user with Id 1 :(");
		  
		   verify(this.userRepositery, times(1)).findById(1);
	   }
	 
	 @Test
	   void testDeleteSuccess() {
		   
		   //Given 
		 HogwartsUser user = new HogwartsUser();
		   user.setId(1);
		   user.setUsername("john");
		   user.setPassword("123456");
		   user.setEnabled(true);
		   user.setRoles("admin user");
		   
		   given(this.userRepositery.findById(1)).willReturn(Optional.of(user));
		   doNothing().when(this.userRepositery).deleteById(1);
		   
		   //When
		   
			   this.userService.delete(1); 
		   
		   //Then 
		   verify(this.userRepositery, times(1)).deleteById(1);

	   }
	 
	 @Test
	   void testDeleteNotFound() {
		   
		   //Given 
		   given(this.userRepositery.findById(1)).willReturn(Optional.empty());
		 
		   //When
		   Throwable thrown = assertThrows(ObjectNotFoundException.class, ()->{
			   this.userService.delete(1); 
		   });
		   
		   //Then 
		   assertThat(thrown)
			  .isInstanceOf(ObjectNotFoundException.class)
			  .hasMessage("Could not find user with Id 1 :(");
			  
			   verify(this.userRepositery, times(1)).findById(1);

	   }

}
