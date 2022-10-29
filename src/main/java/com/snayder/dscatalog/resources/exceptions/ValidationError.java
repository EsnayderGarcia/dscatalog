package com.snayder.dscatalog.resources.exceptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<FieldMessage> errors = new ArrayList<>();
	
	public ValidationError() {
		super();
	}
	
	public List<FieldMessage> getErrors() {
		return errors;
	}

	public void addError(FieldMessage error) {
		errors.add(error);
	}
	
}
