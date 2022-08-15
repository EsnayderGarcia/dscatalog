package com.snayder.dscatalog.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import com.snayder.dscatalog.dtos.ProductDTO;
import com.snayder.dscatalog.services.ProductService;

@RestController
@RequestMapping("/products")
public class ProductResource {
	
	@Autowired
	private ProductService productService;
	
	@GetMapping
	public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable) {
		
		Page<ProductDTO> products = this.productService.findAllPaged(pageable);
		return ResponseEntity.ok(products);
	}
	
	@GetMapping("/{idProduct}")
	public ResponseEntity<ProductDTO> findById(@PathVariable Long idProduct) {
		ProductDTO product = this.productService.findById(idProduct);
		return ResponseEntity.ok(product);
	}
	
	@PostMapping
	public ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO dto) {
		
		dto = this.productService.insert(dto);
		
		/*Uri de acesso a nova categoria criada!*/
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
					 .path("/{id}")
					 .buildAndExpand(dto.getId())
					 .toUri();
		
		return ResponseEntity.created(uri).body(dto);
	}
	
	@PutMapping("/{idProduct}")
	public ResponseEntity<ProductDTO> update(@RequestBody ProductDTO dto, 
			@PathVariable Long idProduct) {
		
		dto = this.productService.update(idProduct, dto);
		return ResponseEntity.ok(dto);
	}
	
	@DeleteMapping("/{idProduct}")
	public ResponseEntity<Void> delete(@PathVariable Long idProduct) {
		this.productService.delete(idProduct);
		return ResponseEntity.noContent().build();
	}
}
