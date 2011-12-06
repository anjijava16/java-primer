package com.sc.ch02.jaxb.annotation;


public class EntryType {
	public String id;
	public String program;
	public String artists;

	public EntryType() {
	}

	public EntryType(String id,String artists, String program) {
		this.id = id;
		this.artists = artists;
		this.program = program;
	}
}
