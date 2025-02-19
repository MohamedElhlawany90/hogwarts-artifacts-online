package com.global.wizard.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.global.wizard.Wizard;
import com.global.wizard.dto.WizardDto;

@Component
public class WizardDtoToWizardConverter implements Converter<WizardDto, Wizard>{

	@Override
	public Wizard convert(WizardDto source) {


		Wizard wizard = new Wizard();
		
	                	wizard.setId(source.id());
		                wizard.setName(source.name());
		
		
		return wizard;
	}

}
