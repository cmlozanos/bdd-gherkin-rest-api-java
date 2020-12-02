package com.example.gherkin.server;

public class User {
	private final Integer id;
	private final String name;
	private final String dni;
	private final String email;

	public User(final Integer id, final String name, final String dni, final String email) {
		super();
		this.id = id;
		this.name = name;
		this.dni = dni;
		this.email = email;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDni() {
		return dni;
	}

	public String getEmail() {
		return email;
	}

}
