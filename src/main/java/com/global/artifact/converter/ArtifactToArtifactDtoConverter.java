package com.global.artifact.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.global.artifact.Artifact;
import com.global.artifact.dto.ArtifactDto;
import com.global.wizard.converter.WizardToWizardDtoConverter;


@Component
public class ArtifactToArtifactDtoConverter implements Converter<Artifact, ArtifactDto> {

	
	private final WizardToWizardDtoConverter wizardToWizardDtoConverter ;
	
	
	public ArtifactToArtifactDtoConverter(WizardToWizardDtoConverter wizardToWizardDtoConverter) {
		
		this.wizardToWizardDtoConverter = wizardToWizardDtoConverter;
	}


	@Override
	public ArtifactDto convert(Artifact source) {


		ArtifactDto artifactDto = new ArtifactDto(source.getId(),
			                                      source.getName(),
			                                      source.getDescription(),
			                                      source.getImageUrl(),
			                                      source.getOwner() != null 
			                                      ? wizardToWizardDtoConverter.convert(source.getOwner()) 
			                                       : null);
		return artifactDto;
	
	
	}

}
 	