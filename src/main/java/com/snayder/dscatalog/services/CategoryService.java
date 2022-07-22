package com.snayder.dscatalog.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snayder.dscatalog.dtos.CategoryDTO;
import com.snayder.dscatalog.entities.Category;
import com.snayder.dscatalog.repositories.CategoryRepository;
import com.snayder.dscatalog.services.exceptions.ResourceNotFoundException;

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
	
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Category category = this.categoryRepository
								.findById(id)
								.orElseThrow(() -> new ResourceNotFoundException("Categoria não Encontrada!"));
		
		return new CategoryDTO(category);
	}
	
	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		
		Category category = new Category();
		
		category.setName(dto.getName());
		category = this.categoryRepository.save(category);
		
		return new CategoryDTO(category);
	}
	
	@Transactional
	public CategoryDTO update(Long idCategory, CategoryDTO dto) {
		
		try {
			Category category = this.categoryRepository.getOne(idCategory);
			
			category.setName(dto.getName());
			category = this.categoryRepository.save(category);
			
			return new CategoryDTO(category);
		} 
		catch (EntityNotFoundException ex) {
			throw new ResourceNotFoundException("Id "+idCategory+ " não encontrado!");
		}
	}
	
}
