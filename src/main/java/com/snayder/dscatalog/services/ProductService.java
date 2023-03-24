package com.snayder.dscatalog.services;

import javax.persistence.EntityNotFoundException;

import com.snayder.dscatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snayder.dscatalog.dtos.CategoryDTO;
import com.snayder.dscatalog.dtos.ProductDTO;
import com.snayder.dscatalog.entities.Category;
import com.snayder.dscatalog.entities.Product;
import com.snayder.dscatalog.repositories.ProductRepository;
import com.snayder.dscatalog.services.exceptions.DataBaseException;
import com.snayder.dscatalog.services.exceptions.ResourceNotFoundException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*Essa anotação vai registrar esta classe como um componente*/ 
/*que vai participar do sistema de injeção de dependência do spring*/
@Service
public class ProductService {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(String name, Long categoryId, double minPrice, double maxPrice, Pageable pageable) {
		List<Category> categories = categoryId == 0 ? null : Arrays.asList(categoryRepository.getOne(categoryId));

		/*O Pageable já é um Stream*/
		Page<Product> products = this.productRepository.find(name.trim(), categories, minPrice, maxPrice, pageable);

		productRepository.findProductsAndCategories(products.stream().collect(Collectors.toSet()));
		return products.map(p -> new ProductDTO(p, p.getCategories()));
	}
	
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Product product = this.productRepository
			.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Produto não Encontrado!"));

		return new ProductDTO(product, product.getCategories());
	}
	
	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product product = new Product();

		this.convertToProduct(dto, product);
		product = this.productRepository.save(product);
		
		return new ProductDTO(product, product.getCategories());
	}
	
	@Transactional
	public ProductDTO update(Long idProduct, ProductDTO dto) {
		try {
			Product product = this.productRepository.getOne(idProduct);
			
			product.getCategories().clear();
			this.convertToProduct(dto, product);
			product = this.productRepository.save(product);
			
			return new ProductDTO(product, product.getCategories());
		} 
		catch (EntityNotFoundException ex) {
			throw new ResourceNotFoundException("Produto não encontrado para atualização");
		}
	}

	public void delete(Long idProduct) {
		try {
			this.productRepository.deleteById(idProduct);
		} 
		catch (EmptyResultDataAccessException ex) {
			throw new ResourceNotFoundException("Produto não encontrado para exclusão");
		}
		catch(DataIntegrityViolationException ex) {
			throw new DataBaseException("Está operação viola a integridade do Banco de Dados");
		}
	}
	
	private void convertToProduct(ProductDTO dto, Product product) {
		product.setName(dto.getName());
		product.setDescription(dto.getDescription());
		product.setPrice(dto.getPrice());
		product.setImgUrl(dto.getImgUrl());
		product.setDate(dto.getdate());
		
		for (CategoryDTO catDTO : dto.getCategories()) {
			product.getCategories().add(new Category(catDTO));
		}
	}

}


