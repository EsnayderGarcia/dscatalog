package com.snayder.dscatalog.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.snayder.dscatalog.dtos.CategoryDTO;
import com.snayder.dscatalog.services.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryResource {
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping
	public ResponseEntity<Page<CategoryDTO>> findAll(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "5") Integer linesPerPage,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction,
			@RequestParam(value = "orderBy", defaultValue = "name") String orderBy
			) {
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		Page<CategoryDTO> categories = this.categoryService.findAllPaged(pageRequest); 
		return ResponseEntity.ok(categories);
	}
	
	@GetMapping("/{idCategory}")
	public ResponseEntity<CategoryDTO> findById(@PathVariable Long idCategory) {
		CategoryDTO category = this.categoryService.findById(idCategory);
		return ResponseEntity.ok(category);
	}
	
	@PostMapping
	public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO dto) {
		
		dto = this.categoryService.insert(dto);
		
		/*Uri de acesso a nova categoria criada!*/
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				                             .path("/{id}")
				                             .buildAndExpand(dto.getId())
				                             .toUri();
		
		return ResponseEntity.created(uri).body(dto);
	}
	
	@PutMapping("/{idCategory}")
	public ResponseEntity<CategoryDTO> update(@RequestBody CategoryDTO dto, 
			@PathVariable Long idCategory) {
		
		dto = this.categoryService.update(idCategory, dto);
		return ResponseEntity.ok(dto);
	}
	
	@DeleteMapping("/{idCategory}")
	public ResponseEntity<Void> delete(@PathVariable Long idCategory) {
		this.categoryService.delete(idCategory);
		return ResponseEntity.noContent().build();
	}
	
}
