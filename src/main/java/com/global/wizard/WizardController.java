package com.global.wizard;

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
import com.global.system.Result;
import com.global.system.StatusCode;
import com.global.wizard.converter.WizardDtoToWizardConverter;
import com.global.wizard.converter.WizardToWizardDtoConverter;
import com.global.wizard.dto.WizardDto;

import jakarta.validation.Valid;


@RestController
@RequestMapping("${api.endpoint.base-url}/wizards")
public class WizardController {
	
	private final WizardService wizardService;
	
	private final WizardToWizardDtoConverter wizardToWizardDtoConverter;
	
	private final WizardDtoToWizardConverter wizardDtoToWizardConverter ;

	public WizardController(WizardService wizardService, WizardToWizardDtoConverter wizardToWizardDtoConverter, WizardDtoToWizardConverter wizardDtoToWizardConverter) {
		
		this.wizardService = wizardService;
		this.wizardToWizardDtoConverter = wizardToWizardDtoConverter;
		this.wizardDtoToWizardConverter = wizardDtoToWizardConverter;
	}
		
	@GetMapping("/{wizardId}")
		public Result findWizarfById(@PathVariable Integer wizardId) {
			
		Wizard foundWizard = this.wizardService.findById(wizardId);
		WizardDto wizardDto = this.wizardToWizardDtoConverter.convert(foundWizard);
		
			return new Result(true, StatusCode.SUCCESS, "Find One Success", wizardDto);
		
	}
	@GetMapping
	public Result findAllWizards() {
       List<Wizard> foundWizards = this.wizardService.findAll();
		
		// convert foundWizards to list of wizardsDtos
		
		List<WizardDto> wizardDtos = foundWizards.stream()
		        .map(foundWizard -> 
		                this.wizardToWizardDtoConverter.convert(foundWizard))
		                .collect(Collectors.toList());
		
		return new Result (true, StatusCode.SUCCESS,"Find All Success",wizardDtos) ;
	}
	

	@PostMapping
	public Result addWizard(@Valid @RequestBody WizardDto wizardDto) {
		// convert wizardDto to wizard 
	    Wizard newWizard = this.wizardDtoToWizardConverter.convert(wizardDto);
	    Wizard savedWizard =  this.wizardService.save(newWizard);
	    WizardDto savedWizardDto = this.wizardToWizardDtoConverter.convert(savedWizard);
	    
		
		return new Result (true, StatusCode.SUCCESS,"Add Success",savedWizardDto) ;
	}
	
	@PutMapping("/{wizardId}")
	public Result updateWizard(@PathVariable Integer wizardId,@RequestBody @Valid WizardDto wizardDto) {
		
		Wizard update = this.wizardDtoToWizardConverter.convert(wizardDto);
		Wizard updatedWizard = this.wizardService.update(wizardId, update);
		WizardDto updatedWizardDto = this.wizardToWizardDtoConverter.convert(updatedWizard);
		
		return new Result(true, StatusCode.SUCCESS, "Update Success", updatedWizardDto) ;
	}
	
	@DeleteMapping("/{wizardId}")
	public Result deleteWizard(@PathVariable Integer wizardId) {
		
		this.wizardService.delete(wizardId);
		return new Result(true,StatusCode.SUCCESS, "Delete Success") ;
		
	}

}
