package com.global.artifact;

public class ArtifactNotFoundExeption extends RuntimeException{
	
	
	public ArtifactNotFoundExeption(String id) {
		super("Could not found artifact with Id " + id + " :(" ); 
		
		
	}

}
