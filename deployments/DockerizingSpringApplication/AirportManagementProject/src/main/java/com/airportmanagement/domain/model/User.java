package com.airportmanagement.domain.model;

public class User {

	String name;
	Integer id;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "User [name=" + name + ", id=" + id + "]";
	}
	
}
