package com.snayder.dscatalog.dtos;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserInsertDTO extends UserDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotBlank(message = "A senha é obrigatória")
	@Size(min = 5, message = "A senha deve conter ao menos {min} caracteres")
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










