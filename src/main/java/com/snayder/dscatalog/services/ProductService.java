package com.snayder.dscatalog.services;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snayder.dscatalog.dtos.ProductDTO;
import com.snayder.dscatalog.entities.Product;
import com.snayder.dscatalog.repositories.ProductRepository;
import com.snayder.dscatalog.services.exceptions.DataBaseException;
import com.snayder.dscatalog.services.exceptions.ResourceNotFoundException;

/*Essa anotação vai registrar esta classe como um componente*/ 
/*que vai participar do sistema de injeção de dependência do spring*/
@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
			
		Page<Product> categories = this.productRepository.findAll(pageRequest);
		
		/*O page já é um stream*/
		Page<ProductDTO> dtos = categories.map(c -> new ProductDTO(c));
										   
		return dtos;
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
		
		product.setName(dto.getName());
		product = this.productRepository.save(product);
		
		return new ProductDTO(product);
	}
	
	@Transactional
	public ProductDTO update(Long idProduct, ProductDTO dto) {
		
		try {
			Product product = this.productRepository.getOne(idProduct);
			
			product.setName(dto.getName());
			product = this.productRepository.save(product);
			
			return new ProductDTO(product);
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

}


