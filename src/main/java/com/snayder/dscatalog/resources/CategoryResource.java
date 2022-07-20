package com.snayder.dscatalog.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snayder.dscatalog.dtos.CategoryDTO;
import com.snayder.dscatalog.services.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryResource {
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping
	public ResponseEntity<List<CategoryDTO>> findAll() {
		List<CategoryDTO> categories = this.categoryService.findAll(); 
		return ResponseEntity.ok(categories);
	}
	
	@GetMapping("/{idCategory}")
	public ResponseEntity<CategoryDTO> findById(@PathVariable Long idCategory) {
		CategoryDTO category = this.categoryService.findById(idCategory);
		return ResponseEntity.ok(category);
	}
	
	
}
