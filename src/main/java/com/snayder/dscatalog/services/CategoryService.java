package com.snayder.dscatalog.services;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snayder.dscatalog.dtos.CategoryDTO;
import com.snayder.dscatalog.entities.Category;
import com.snayder.dscatalog.repositories.CategoryRepository;

/*Essa anotação vai registrar esta classe como um componente*/ 
/*que vai participar do sistema de injeção de dependência do spring*/
@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
			
		List<Category> categories = this.categoryRepository.findAll();
		List<CategoryDTO> dtos = categories.stream()
										   .map(c -> new CategoryDTO(c))
										   .collect(Collectors.toList());
		return dtos;
	}
	
	
	
	
	
	
	
}
