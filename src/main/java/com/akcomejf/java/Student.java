package com.akcomejf.java;

import java.io.Serializable;

public class Student implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7360664246813838844L;
	
	private Long id;
	
	private String name;
	
	private int age;

	public Student(Long id, String name, int age) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	

}
