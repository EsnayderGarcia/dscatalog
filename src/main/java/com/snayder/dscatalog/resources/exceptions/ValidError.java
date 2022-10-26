package com.snayder.dscatalog.resources.exceptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ValidError extends StandardError implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<FieldError> errors = new ArrayList<>();
	
	public ValidError() {
		super();
	}
	
	public List<FieldError> getErrors() {
		return errors;
	}

	public void addError(FieldError error) {
		errors.add(error);
	}
	
}
