package com.global.wizard;

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
import com.global.artifact.Artifact;
import com.global.system.StatusCode;
import com.global.system.Exeption.ObjectNotFoundException;
import com.global.wizard.dto.WizardDto;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class WizardControllerTest {

	
	@Autowired
	MockMvc mockMvc ;
	
	@MockitoBean
	WizardService  wizardService ;
	
	@Autowired
	ObjectMapper objectMapper;
	
	List<Wizard> wizards;
	
	@Value("${api.endpoint.base-url}")
	String baseUrl ;
	
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
		
		Artifact a3 = new Artifact();
		a3.setId("1250808601744904193");
		a3.setName("Elder Wand");
		a3.setDescription("The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny");
		a3.setImageUrl("ImageUrl");
		
		Artifact a4 = new Artifact();
		a4.setId("1250808601744904194");
		a4.setName("The Marauder's Map");
		a4.setDescription("A magical map of Hogwarts created by Remus Lupin.");
		a4.setImageUrl("ImageUrl");
		
		Artifact a5 = new Artifact();
		a5.setId("1250808601744904195");
		a5.setName("The Sword Of Gryffindor");
		a5.setDescription("A goblin-made sword adorned with large rubies on the pommel.");
		a5.setImageUrl("ImageUrl");
		
		Artifact a6 = new Artifact();
		a6.setId("1250808601744904196");
		a6.setName("Resurrection Stone");
		a6.setDescription("The Resurrection Stone allows the holder to bring back deceased loved ones.");
		a6.setImageUrl("ImageUrl");
		
		
		this.wizards = new ArrayList<>();
		
		Wizard w1 = new Wizard();
		w1.setId(1);
		w1.setName("Albus Dumbledore");
		w1.addArtifact(a1);
		w1.addArtifact(a3);
		this.wizards.add(w1);
		
		
		Wizard w2 = new Wizard();
		w2.setId(2);
		w2.setName("Harry Potter");
		w2.addArtifact(a2);
		w2.addArtifact(a4);
		this.wizards.add(w2);
		
		Wizard w3 = new Wizard();
		w3.setId(3);
		w3.setName("Neville Longbottom");
		w3.addArtifact(a5);
		this.wizards.add(w3);
		
	}
	
	@AfterEach
	void tearDown() throws Exception {
	}


	   @Test
	   void testFindAllWizardsSuccess() throws Exception {
		    
		        // Given
				 given(this.wizardService.findAll()).willReturn(this.wizards);
				
				// When And Then
				this.mockMvc.perform(get(this.baseUrl +"/wizards").accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.flag").value(true))
				.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
				.andExpect(jsonPath("$.message").value("Find All Success"))
				.andExpect(jsonPath("$.data", Matchers.hasSize(this.wizards.size())))
				.andExpect(jsonPath("$.data[0].id").value(1))
				.andExpect(jsonPath("$.data[0].name").value("Albus Dumbledore"))
				.andExpect(jsonPath("$.data[1].id").value(2))
				.andExpect(jsonPath("$.data[1].name").value("Harry Potter"));
		   
	   }
	@Test
	void testFindWizardByIdSuccess() throws Exception {
		//Given
		
		given(this.wizardService.findById(1)).willReturn(this.wizards.get(0));
		
		//When & Then
		this.mockMvc.perform(get(this.baseUrl +"/wizards/1").accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.flag").value(true))
		.andExpect(jsonPath( "$.code").value(StatusCode.SUCCESS))
		.andExpect(jsonPath( "$.message").value("Find One Success"))
		.andExpect(jsonPath( "$.data.id").value(1))
		.andExpect(jsonPath( "$.data.name").value("Albus Dumbledore")) ;
		
	}
	
	@Test
	void testFindWizardByIdNotFound() throws Exception  {
		
		//Given
		given(this.wizardService.findById(5))
		.willThrow(new ObjectNotFoundException("wizard", 5));
		
		//When and Then
		this.mockMvc.perform(get(this.baseUrl +"/wizards/5").accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath( "$.flag").value(false))
		.andExpect(jsonPath( "$.code").value(StatusCode.NOT_FOUND))
		.andExpect(jsonPath( "$.message").value("Could not find wizard with Id 5 :("))
		.andExpect(jsonPath( "$.data").isEmpty()); 
		
	}

   @Test
	void testAddWizardSuccess() throws Exception {
		
		// Given
		WizardDto wizardDto = new WizardDto(null
				                                  ,"Hermione Granger"
				                                  , 2
				                                  );
		
		String json =  this.objectMapper.writeValueAsString(wizardDto);
		
		
		 Wizard savedWizard = new Wizard();
		 savedWizard.setId(4);
		 savedWizard.setName("Hermione Granger");
		 
		 given(this.wizardService.save(Mockito.any(Wizard.class))).willReturn(savedWizard);
		
		
		//When and Then
		 
		 this.mockMvc.perform(post(this.baseUrl +"/wizards").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.flag").value(true))
			.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
			.andExpect(jsonPath("$.message").value("Add Success"))
			.andExpect(jsonPath("$.data.id").isNotEmpty())
			.andExpect(jsonPath("$.data.name").value(savedWizard.getName()));

	}
   
   @Test
	void testUpdateWizardSuccesss() throws Exception {
		
		        // Given
				WizardDto wizardDto  = new WizardDto(  null
						                              ,"Updated wizard name"
						                              ,0) ;
				
				String json =  this.objectMapper.writeValueAsString(wizardDto);
				
				
				 Wizard updatedWizard = new Wizard();
				 updatedWizard.setId(1);
				 updatedWizard.setName("Updated wizard name"); 
				 
				 given(this.wizardService.update(eq(1),Mockito.any(Wizard.class))).willReturn(updatedWizard);
				
				
				//When and Then
				 
				 this.mockMvc.perform(put(this.baseUrl +"/wizards/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.flag").value(true))
					.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
					.andExpect(jsonPath("$.message").value("Update Success"))
					.andExpect(jsonPath("$.data.id").value(1))
					.andExpect(jsonPath("$.data.name").value("Updated wizard name"));
	}
   
	
	@Test
	void testUpdateWizardErrorWithNoExistenId() throws Exception {
		
		// Given
		WizardDto wizardDto  = new WizardDto(  5
                                              ,"Updated wizard name" 
                                              , 0) ;
		
		String json =  this.objectMapper.writeValueAsString(wizardDto);
		
		 
		 given(this.wizardService.update(eq(5),Mockito.any(Wizard.class))).willThrow(new ObjectNotFoundException("wizard", 5));
		
		
		//When and Then
		 
		 
		 this.mockMvc.perform(put(this.baseUrl +"/wizards/5").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.flag").value(false))
			.andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
			.andExpect(jsonPath("$.message").value("Could not find wizard with Id 5 :("))
			.andExpect(jsonPath("$.data").isEmpty());
	}
	
   @Test
	void testDeleteWizardSuccess() throws Exception {
		//Given
		
		doNothing().when(this.wizardService).delete(3);
		
		//When & Then
		this.mockMvc.perform(delete(this.baseUrl +"/wizards/3").accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.flag").value(true))
		.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
		.andExpect(jsonPath("$.message").value("Delete Success"))
		.andExpect(jsonPath("$.data").isEmpty());
		
		
	}
	
	
	@Test
	void testDeleteWizardErrorWithNonExistenId() throws Exception {
		//Given
		
		doThrow(new ObjectNotFoundException("wizard", 5)).when(this.wizardService).delete(5);
		
		//When & Then
		this.mockMvc.perform(delete(this.baseUrl +"/wizards/5").accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.flag").value(false))
		.andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
		.andExpect(jsonPath("$.message").value("Could not find wizard with Id 5 :("))
		.andExpect(jsonPath("$.data").isEmpty());
		
	}
   
	@Test
	void testAssignArtifactSuccess() throws Exception {
		//Given
		doNothing().when(this.wizardService).assignArtifact(2, "1250808601744904191");

		//When & Then
		this.mockMvc.perform(put(this.baseUrl +"/wizards/2/artifacts/1250808601744904191").accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.flag").value(true))
		.andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
		.andExpect(jsonPath("$.message").value("Artifact Assignment Success"))
		.andExpect(jsonPath("$.data").isEmpty());
	}
	
	@Test
	void testAssignArtifactErrorWithNonExsistantWizardId() throws Exception {
		//Given
		doThrow(new ObjectNotFoundException("wizard", 5)).when(this.wizardService).assignArtifact(5, "1250808601744904191");

		//When & Then
		this.mockMvc.perform(put(this.baseUrl +"/wizards/5/artifacts/1250808601744904191").accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.flag").value(false))
		.andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
		.andExpect(jsonPath("$.message").value("Could not find wizard with Id 5 :("))
		.andExpect(jsonPath("$.data").isEmpty());
	}
   
	@Test
	void testAssignArtifactErrorWithNonExsistantArtifactId() throws Exception {
		//Given
		doThrow(new ObjectNotFoundException("artifact", "1250808601744904199")).when(this.wizardService).assignArtifact(2, "1250808601744904199");

		//When & Then
		this.mockMvc.perform(put(this.baseUrl +"/wizards/2/artifacts/1250808601744904199").accept(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.flag").value(false))
		.andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
		.andExpect(jsonPath("$.message").value("Could not find artifact with Id 1250808601744904199 :("))
		.andExpect(jsonPath("$.data").isEmpty());
	}
}
