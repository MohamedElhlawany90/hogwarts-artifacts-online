package com.global.artifact;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
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

import com.global.artifact.utils.IdWorker;
import com.global.system.Exeption.ObjectNotFoundException;
import com.global.wizard.Wizard;

@ExtendWith(MockitoExtension.class)
class ArtifactServiceTest {
	
	@Mock
	ArtifactRepository artifactRepository ;
	
	@Mock
	IdWorker idWorker ;
	
	@InjectMocks
	ArtifactService artifactService ;
	
	List<Artifact> artifacts ;

	@BeforeEach
	void setUp() throws Exception {
		
		Artifact a1 = new Artifact();
		a1.setId("1250808601744904191");
		a1.setName("Deluminator");
		a1.setDescription("Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter.");
		a1.setImageUrl("ImageUrl");
		
		
		Artifact a2 = new Artifact();
		a2.setId("1250808601744904192");
		a2.setName("Invisibility Cloak");
		a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
		a2.setImageUrl("ImageUrl");
		
		this.artifacts = new ArrayList<>();
		this.artifacts.add(a1);
		this.artifacts.add(a2);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testFindByIdSuccess() {


		// Given. Arrange input and targets . Define the behavior of Mock object artifactRepository.
		Artifact a = new Artifact();
		a.setId("1250808601744904192");
		a.setName("Invisibility cloak");
		a.setDescription("an Invisibility cloak is used to make the wearer invisible");
		a.setImageUrl("ImageUrl");
		
		Wizard w = new Wizard();
		w.setId(2);
		w.setName("Harry Poter");
		
		a.setOwner(w);
		
		given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(a)); // Define the behavior of the Mock object.
		
		//When. Act on the target behavior . When steps should cover the method to be tested
		Artifact returnedArtifact  = artifactService.findById("1250808601744904192");
		
		//Then. Assert expected outcomes . 
		assertThat(returnedArtifact.getId()).isEqualTo(a.getId());
		assertThat(returnedArtifact.getName()).isEqualTo(a.getName());
		assertThat(returnedArtifact.getDescription()).isEqualTo(a.getDescription());
		assertThat(returnedArtifact.getImageUrl()).isEqualTo(a.getImageUrl());
		
		verify(artifactRepository, times(1)).findById("1250808601744904192");
		
	}
	
	   @Test
       void testFindByIdNotFound(){
    	   
		   //Given
		   given(artifactRepository.findById(Mockito.any(String.class))).willReturn(Optional.empty());
    	   
		   //When انت هنا بتهندل الامور لو المستخدم دخل اى دى مش موجود
		   Throwable thrown = catchThrowable(()->{
			   
				Artifact returnedArtifact  = artifactService.findById("1250808601744904192");

			   
		   });
		   
		   //Then  انت هنا مستنى الاكسبشن ودا هياكد لو كانت فعلا الميثود اللى قبلك مسكته وعملتله ثرو ولا لا
		   assertThat(thrown)
		   .isInstanceOf(ObjectNotFoundException.class)
		   .hasMessage("Could not find artifact with Id 1250808601744904192 :("); 
		   
			verify(artifactRepository, times(1)).findById("1250808601744904192");

		   // This is an expect message when you write (has message make sure it will be the exact message that you will write at the class "ArtifactNotFoundExeption" at this case)
       }
	   
	   @Test
	   void testFIndAllSuccess() {
		   // Given
		   given(artifactRepository.findAll()).willReturn(this.artifacts);
		   
		   // When
		   List<Artifact> acuallArtifacts = artifactService.findAll();
		   
		   //Then
		   assertThat(acuallArtifacts.size()).isEqualTo(this.artifacts.size());
		   
		   verify(artifactRepository, times(1)).findAll();
		   
		   
	   }
	   
	   @Test
	   void testSaveSuccess() {
		   
		   // Given
		   Artifact newArtifact = new Artifact();
		   newArtifact.setName("Artifact 3");
		   newArtifact.setDescription("Description...");
		   newArtifact.setImageUrl("ImageUrl...");
		   
		   given(idWorker.nextId()).willReturn(123456L);
		   given(artifactRepository.save(newArtifact)).willReturn(newArtifact);
		   
		   
		   // When
		   
		   Artifact SaveArtifact = artifactService.save(newArtifact);
		   
		   // Then
		   
		   assertThat(SaveArtifact.getId()).isEqualTo("123456");
		   assertThat(SaveArtifact.getName()).isEqualTo(newArtifact.getName());
		   assertThat(SaveArtifact.getDescription()).isEqualTo(newArtifact.getDescription());
		   assertThat(SaveArtifact.getImageUrl()).isEqualTo(newArtifact.getImageUrl());
		   
		   verify(artifactRepository, times(1)).save(newArtifact);
		   
	   }
	   
	   @Test
	   void testUpdateSuccess() {
		   
		   //Given
		   Artifact oldArtifact = new Artifact();
		   oldArtifact.setId("1250808601744904192");
		   oldArtifact.setName("Invisibility cloak");
		   oldArtifact.setDescription("an Invisibility cloak is used to make the wearer invisible");
		   oldArtifact.setImageUrl("ImageUrl");
		   
		   
		   Artifact update = new Artifact();
		   update.setId("1250808601744904192");
		   update.setName("Invisibility cloak");
		   update.setDescription("A new description");
		   update.setImageUrl("ImageUrl");
		   
		   given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(oldArtifact));
		   given(artifactRepository.save(oldArtifact)).willReturn(oldArtifact);
		   
		   // When
		   
		   Artifact  updatedArtifact = artifactService.update("1250808601744904192", update);
		   
		   //Then
		   
		   assertThat(updatedArtifact.getId()).isEqualTo(update.getId());
		   assertThat(updatedArtifact.getDescription()).isEqualTo(update.getDescription());
		   
		   verify(artifactRepository, times(1)).findById("1250808601744904192");
		   verify(artifactRepository, times(1)).save(oldArtifact);

		   
	   }
	   
	   @Test
	   void testUpdateNotFound() {
		   
		   // Given
		   Artifact update = new Artifact();
		   
		   update.setName("Invisibility cloak");
		   update.setDescription("an Invisibility cloak is used to make the wearer invisible");
		   update.setImageUrl("ImageUrl");
		   
		   given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.empty());
		   
		   // When
		   
		  assertThrows(ObjectNotFoundException.class, ()->{
			  
			  artifactService.update("1250808601744904192", update);
		  });
		   
		   // Then
		  
		   verify(artifactRepository, times(1)).findById("1250808601744904192");
	   }
	   
	   @Test
	   void testDeleteSuccess() {
		   
		   //Given 
		   given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.empty());
		 
		   
		   //When
		   assertThrows(ObjectNotFoundException.class, ()->{
			   artifactService.delete("1250808601744904192"); 
		   });
		   
		   //Then 
		   verify(artifactRepository, times(1)).findById("1250808601744904192");

		   
	   }

}
