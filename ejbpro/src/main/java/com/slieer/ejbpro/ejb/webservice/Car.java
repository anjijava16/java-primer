package com.slieer.ejbpro.ejb.webservice;

public class Car {

	private String name;
	private String message;
	
	public Car() {
		super();
	}

	public Car(String name, String message) {
		super();
		this.name = name;
		this.message = message;
	}

	public String getName() {
		return name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Car [name=" + name + ", message=" + message + "]";
	}
}