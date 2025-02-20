package com.global.wizard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import com.global.artifact.Artifact;
import com.global.artifact.ArtifactRepository;
import com.global.system.Exeption.ObjectNotFoundException;

@ExtendWith(MockitoExtension.class)
class WizardServiceTest {

	
	@Mock
	WizardRepository wizardRepository ; 
	
	@Mock
	ArtifactRepository artifactRepository;
	
	
	@InjectMocks
	WizardService wizardService ;
	
	List<Wizard> wizards ;
	
	
	@BeforeEach
	void setUp() throws Exception {
		
        this.wizards = new ArrayList<>();
		
		Wizard w1 = new Wizard();
		w1.setId(1);
		w1.setName("Albus Dumbledore");
		this.wizards.add(w1);
		
		Wizard w2 = new Wizard();
		w2.setId(2);
		w2.setName("Harry Potter");
		this.wizards.add(w2);
		
		Wizard w3 = new Wizard();
		w3.setId(3);
		w3.setName("Neville Longbottom");
		this.wizards.add(w3);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testFindByIdSuccess() {
		
		//Given

		Wizard w2 = new Wizard();
		w2.setId(2);
		w2.setName("Harry Potter");
		this.wizards.add(w2);
		
		given(this.wizardRepository.findById(2)).willReturn(Optional.of(w2));
		
		//When
		Wizard returnedWizard = this.wizardService.findById(2);
		
		//Then
		
		assertThat(returnedWizard.getId()).isEqualTo(w2.getId());
		assertThat(returnedWizard.getName()).isEqualTo(w2.getName());
		assertThat(returnedWizard.getArtifacts()).isEqualTo(w2.getArtifacts());
		
		verify(this.wizardRepository, times(1)).findById(2);
	}
	
	@Test
	void testFindByIdNotFound() {
		//Given
		given(this.wizardRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());
		
		//When
		Throwable thrown = catchThrowable(()->{
			   
			Wizard returnedWizard  = this.wizardService.findById(2);
	   });
		
		//Then
		
		 assertThat(thrown)
		   .isInstanceOf(ObjectNotFoundException.class)
		   .hasMessage("Could not find wizard with Id 2 :("); 
		   
			verify(this.wizardRepository, times(1)).findById(2);
		
	}
	
	@Test
	   void testFIndAllSuccess() {
		   // Given
		   given(this.wizardRepository.findAll()).willReturn(this.wizards);
		   
		   // When
		   List<Wizard> actualWizards = this.wizardService.findAll();
		   
		   //Then
		   assertThat(actualWizards.size()).isEqualTo(this.wizards.size());
		   
		   verify(wizardRepository, times(1)).findAll();
		   
	   }
	
	@Test
	   void testSaveSuccess() {
		   
		   // Given
		   Wizard newWizard = new Wizard();
		   newWizard.setName("Hermione Granger");
		  
		   
		   given(this.wizardRepository.save(newWizard)).willReturn(newWizard);
		   
		   // When
		   
		   Wizard returnedWizard = this.wizardService.save(newWizard);
		   
		   // Then
		   
		   assertThat(returnedWizard.getName()).isEqualTo(newWizard.getName());
		   
		   verify(this.wizardRepository, times(1)).save(newWizard);
		   
	   }
	
	 @Test
	   void testUpdateSuccess() {
		   
		   //Given
		   Wizard oldWizard = new Wizard();
		   oldWizard.setId(1);
		   oldWizard.setName("Albus Dumbledore");
		   
		   
		   Wizard update = new Wizard();
		   update.setName("Albus Dumbledore - update");
		   
		   given(this.wizardRepository.findById(1)).willReturn(Optional.of(oldWizard));
		   given(this.wizardRepository.save(oldWizard)).willReturn(oldWizard);
		   
		   // When
		   
		   Wizard  updatedWizard = this.wizardService.update(1, update);
		   
		   //Then
		   
		    assertThat(updatedWizard.getId()).isEqualTo(1);  
		    assertThat(updatedWizard.getName()).isEqualTo(update.getName());
		   
		   verify(this.wizardRepository, times(1)).findById(1);
		   verify(this.wizardRepository, times(1)).save(oldWizard);

		   
	   }
	   
	   @Test
	   void testUpdateNotFound() {
		   
		   // Given
		   Wizard update = new Wizard();
		   
		   update.setName("Albus Dumbledore - update");
		   
		   given(this.wizardRepository.findById(1)).willReturn(Optional.empty());
		   
		   // When
		   
		  assertThrows(ObjectNotFoundException.class, ()->{
			  
			  this.wizardService.update(1, update);
		  });
		   
		   // Then
		  
		   verify(this.wizardRepository, times(1)).findById(1);
	   }
	   
	   @Test
	   void testDeleteSuccess() {
		   
		   //Given 
		   Wizard wizard = new Wizard();
		   wizard.setId(1);
		   wizard.setName("Albus Dumbledore");
		   
		   
		   
		   
		   given(this.wizardRepository.findById(1)).willReturn(Optional.of(wizard));
		   doNothing().when(this.wizardRepository).deleteById(1);
		   
		   //When
		   
			   this.wizardService.delete(1); 
		   
		   
		   //Then 
		   verify(this.wizardRepository, times(1)).deleteById(1);

	   }
	   
	   @Test
	   void testDeleteNotFound() {
		   
		   //Given 
		   
		   
		   given(this.wizardRepository.findById(1)).willReturn(Optional.empty());
		 
		   
		   //When
		   assertThrows(ObjectNotFoundException.class, ()->{
			   this.wizardService.delete(1); 
		   });
		   
		   //Then 
		   verify(this.wizardRepository, times(1)).findById(1);

	   }
@Test
void testAssignArtifactSuccess() {
		   
		   //Given
		   
		   Artifact a1 = new Artifact();
			a1.setId("1250808601744904191");
			a1.setName("Deluminator");
			a1.setDescription("Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter.");
			a1.setImageUrl("ImageUrl");
			
			Wizard w2 = new Wizard();
			w2.setId(2);
			w2.setName("Harry Potter");
			w2.addArtifact(a1);
			
			Wizard w3 = new Wizard();
			w3.setId(3);
			w3.setName("Neville Longbottom");
		   
			given(this.artifactRepository.findById("1250808601744904191")).willReturn(Optional.of(a1));
			given(this.wizardRepository.findById(3)).willReturn(Optional.of(w3));
			
		   //When
		   this.wizardService.assignArtifact(3, "1250808601744904191");
			
		   //Then 
		   
		   assertThat(a1.getOwner().getId()).isEqualTo(3);
		   assertThat(w3.getArtifacts()).contains(a1);
	   }

@Test
void testAssignArtifactErrorWithNotExistantWizardId() {
		   
		   //Given
		   
		   Artifact a1 = new Artifact();
			a1.setId("1250808601744904191");
			a1.setName("Deluminator");
			a1.setDescription("Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter.");
			a1.setImageUrl("ImageUrl");
			
			Wizard w2 = new Wizard();
			w2.setId(2);
			w2.setName("Harry Potter");
			w2.addArtifact(a1);
			
		   
			given(this.artifactRepository.findById("1250808601744904191")).willReturn(Optional.of(a1));
			given(this.wizardRepository.findById(3)).willReturn(Optional.empty());
			
		   //When
			Throwable thrown = assertThrows(ObjectNotFoundException.class, ()-> {
				   this.wizardService.assignArtifact(3, "1250808601744904191");

			});
		   //Then 
		   assertThat(thrown).isInstanceOf(ObjectNotFoundException.class).hasMessage("Could not find wizard with Id 3 :(");
		   assertThat(a1.getOwner().getId()).isEqualTo(2);
	   }

@Test
void testAssignArtifactErrorWithNotExistantArtifactId() {
		   
		   //Given
		   
			given(this.artifactRepository.findById("1250808601744904191")).willReturn(Optional.empty());
			
		   //When
			Throwable thrown = assertThrows(ObjectNotFoundException.class, ()-> {
				   this.wizardService.assignArtifact(3, "1250808601744904191");
			});
		  
		 //Then 
		   assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
		   .hasMessage("Could not find artifact with Id 1250808601744904191 :(");
	   
}
}
