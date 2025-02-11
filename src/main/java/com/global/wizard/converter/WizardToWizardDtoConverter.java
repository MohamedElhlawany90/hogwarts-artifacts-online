package com.global.wizard.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.global.wizard.Wizard;
import com.global.wizard.dto.WizardDto;


@Component
public class WizardToWizardDtoConverter implements Converter<Wizard, WizardDto>{

	@Override
	public WizardDto convert(Wizard source) {
		
		WizardDto wizardDto = new WizardDto(source.getId(),
				                      source.getName(),
				                      source.getNumberOfArtifacts());
		
			return wizardDto;
	}

}
