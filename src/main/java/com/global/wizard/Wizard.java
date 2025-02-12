package com.global.wizard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.global.artifact.Artifact;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Wizard implements Serializable{
	
	@Id
	private int id ;
	
	private String name;
	
	@OneToMany(cascade = {CascadeType.PERSIST , CascadeType.MERGE} , mappedBy = "owner")
	private List<Artifact> artifacts = new ArrayList<>() ;
	
	
	public  Wizard() {
		
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public List<Artifact> getArtifacts() {
		return artifacts;
	}


	public void setArtifacts(List<Artifact> artifacts) {
		this.artifacts = artifacts;
	}


	public void addArtifact(Artifact artifact) {
		
		artifact.setOwner(this);
		this.artifacts.add(artifact);
		
	}


	public Integer getNumberOfArtifacts() {
		
		
		return this.artifacts.size();
	}
	
	
	
	

}
