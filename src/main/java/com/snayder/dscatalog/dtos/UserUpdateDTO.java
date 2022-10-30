package com.snayder.dscatalog.dtos;

import java.io.Serializable;

import com.snayder.dscatalog.services.validations.UserUpdateValid;

@UserUpdateValid
public class UserUpdateDTO extends UserDTO implements Serializable {
	private static final long serialVersionUID = 1L;
}
