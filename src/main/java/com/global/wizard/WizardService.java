package com.global.wizard;

import java.util.List;

import org.springframework.stereotype.Service;

import com.global.artifact.Artifact;
import com.global.artifact.ArtifactRepository;
import com.global.artifact.utils.IdWorker;
import com.global.system.Exeption.ObjectNotFoundException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class WizardService {
	
private final WizardRepository wizardRepository;

private final ArtifactRepository artifactRepository;
	
	
	public WizardService(WizardRepository wizardRepository, ArtifactRepository artifactRepository) {
	super();
	this.wizardRepository = wizardRepository;
	this.artifactRepository = artifactRepository;
}
	

	public  Wizard findById(Integer wizardId) {
		
		
		return this.wizardRepository.findById(wizardId)
				.orElseThrow(()-> new ObjectNotFoundException("wizard", wizardId));
	}
	
	public List<Wizard> findAll() {
		
		
		return this.wizardRepository.findAll() ;
	}
	
	public Wizard save(Wizard newWizard) {
		
		return this.wizardRepository.save(newWizard) ;
		
	}
	
	// We are not updating the wizard's Artifacts through this method, we only update the wizard's name
    public Wizard update(Integer wizardId, Wizard update) {
		
		return this.wizardRepository.findById(wizardId)
				.map(oldWizard -> {
					oldWizard.setName(update.getName());
					
					return this.wizardRepository.save(oldWizard);
				})
				.orElseThrow(()-> new ObjectNotFoundException("wizard", wizardId));
	}
    
    public void delete(Integer wizardId) {
		
		 Wizard wizardToBeDeleted = this.wizardRepository.findById(wizardId)
				.orElseThrow(()->new ObjectNotFoundException("wizard", wizardId));
		
		 // Before deletion, we will unassign this wizard's owned artifacts.
		 wizardToBeDeleted.removeAllArtifacts();
		this.wizardRepository.deleteById(wizardId);
		
	}
    
    public void assignArtifact(Integer wizardId , String artifactId) {
    	
    	// Find Artifact by id from DB. 
    	Artifact artifactToBeAssigned = this.artifactRepository.findById(artifactId).orElseThrow(()-> new ObjectNotFoundException("artifact", artifactId));
    	
    	// Find Wizard by id from DB
    	Wizard wizard = this.wizardRepository.findById(wizardId).orElseThrow(()-> new ObjectNotFoundException("wizard", wizardId));

    	
    	// Artifact Assignment
    	// We need to see if the artifact is already owned by some wizard
    	if(artifactToBeAssigned.getOwner() !=null) {
    		artifactToBeAssigned.getOwner().removeArtifact(artifactToBeAssigned);
    	} 
    	wizard.addArtifact(artifactToBeAssigned);
    	
    }

}
