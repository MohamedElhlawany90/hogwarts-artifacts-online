package com.global.artifact;

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

import com.global.artifact.converter.ArtifactDtoToArtifactConverter;
import com.global.artifact.converter.ArtifactToArtifactDtoConverter;
import com.global.artifact.dto.ArtifactDto;
import com.global.system.Result;
import com.global.system.StatusCode;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/artifacts")
public class ArtifactController {
	
	private final ArtifactService artifactService ;
	
	private final ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter;
	
	private final ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter;
	
	
	
	
	public ArtifactController(ArtifactService artifactService, ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter, ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter) {
		super();
		this.artifactService = artifactService;
		this.artifactToArtifactDtoConverter = artifactToArtifactDtoConverter;
		this.artifactDtoToArtifactConverter = artifactDtoToArtifactConverter ;
	}




	@GetMapping("/{artifactId}")
	public Result findArtifactById(@PathVariable String artifactId) {
		Artifact foundArtifact = this.artifactService.findById(artifactId);
		ArtifactDto artifactDto =  this.artifactToArtifactDtoConverter.convert(foundArtifact);
		return new Result(true, StatusCode.SUCCESS, "Find One Success", artifactDto) ;
	}
	
	@GetMapping
	public Result findAllArtifacts() {
		List<Artifact> foundArtifacts = this.artifactService.findAll();
		
		// convert fondArtifacts to list of artifactsDtos
		
		List<ArtifactDto> artifactDtos = foundArtifacts.stream()
		        .map(foundArtifact -> 
		                this.artifactToArtifactDtoConverter.convert(foundArtifact))
		                .collect(Collectors.toList());
		
		return new Result (true, StatusCode.SUCCESS,"Find All Success",artifactDtos) ;
	}
	
	@PostMapping
	public Result addAtrifact(@Valid @RequestBody ArtifactDto artifactDto) {
		// convert artifactDto to artifact 
	    Artifact newArtifact = this.artifactDtoToArtifactConverter.convert(artifactDto);
	    Artifact savedArtifact =  this.artifactService.save(newArtifact);
	    ArtifactDto savedArtifactDto = this.artifactToArtifactDtoConverter.convert(savedArtifact);
	    
		
		return new Result (true, StatusCode.SUCCESS,"Add Success",savedArtifactDto) ;
	}
	
	@PutMapping("/{artifactId}")
	public Result updateArtifact(@PathVariable String artifactId,@RequestBody @Valid ArtifactDto artifactDto) {
		
		Artifact update = this.artifactDtoToArtifactConverter.convert(artifactDto);
		Artifact updatedArtifact = this.artifactService.update(artifactId, update);
		ArtifactDto updatedArtifactDto = this.artifactToArtifactDtoConverter.convert(updatedArtifact);
		
		return new Result(true, StatusCode.SUCCESS, "Update Success", updatedArtifactDto) ;
	}
	
	@DeleteMapping("{artifactId}")
	public Result DeleteArtifact(@PathVariable String artifactId) {
		
		this.artifactService.delete(artifactId);
		return new Result(true,StatusCode.SUCCESS, "Delete Success") ;
	}

}
