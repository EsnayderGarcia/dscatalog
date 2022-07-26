package com.snayder.dscatalog.services;

import com.snayder.dscatalog.dtos.CategoryDTO;
import com.snayder.dscatalog.entities.Category;
import com.snayder.dscatalog.repositories.CategoryRepository;
import com.snayder.dscatalog.services.exceptions.DataBaseException;
import com.snayder.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

/*Essa anotação vai registrar esta classe como um componente*/ 
/*que vai participar do sistema de injeção de dependência do spring*/
@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		List<Category> categories = this.categoryRepository.findAll();
		return categories.stream().map(CategoryDTO::new).collect(Collectors.toList());
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
			throw new ResourceNotFoundException("Categoria não encontrada para atualização");
		}
	}

	public void delete(Long idCategory) {
		try {
			this.categoryRepository.deleteById(idCategory);
		} 
		catch (EmptyResultDataAccessException ex) {
			throw new ResourceNotFoundException("Categoria não encontrada para exclusão");
		}
		catch(DataIntegrityViolationException ex) {
			throw new DataBaseException("Está operação viola a integridade do Banco de Dados");
		}
	}

}


