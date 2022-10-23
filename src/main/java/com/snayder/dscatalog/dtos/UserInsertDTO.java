package com.snayder.dscatalog.dtos;

import java.io.Serializable;

public class UserInsertDTO extends UserDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String password;
	
	public UserInsertDTO() {
		super();
	}
		
	public UserInsertDTO(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}










