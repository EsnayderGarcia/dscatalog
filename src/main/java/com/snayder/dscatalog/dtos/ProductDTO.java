package com.snayder.dscatalog.dtos;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;

import com.snayder.dscatalog.entities.Category;
import com.snayder.dscatalog.entities.Product;

public class ProductDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	private Long id;
	
	@NotBlank(message = "O nome do produto é obrigatório")
	private String name;
	
	@NotBlank(message = "A descrição do produto é obrigatório")
	private String description;
	
	@Positive(message = "O preço do produto deve ser um valor positivo")
	@NotNull(message = "O preço do produto é obrigatório")
	private Double price;
	
	private String imgUrl;
	
	@PastOrPresent(message = "A data não pode ser futura")
	private Instant date;
	
	@NotEmpty(message = "As categorias do produto devem ser informadas")
	@NotNull(message = "As categorias do produto devem ser informadas")
	private final List<CategoryDTO> categories = new ArrayList<>();
	
	public ProductDTO() {}

	public ProductDTO(Long id, String name, String description, Double price, String imgUrl, Instant date) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.imgUrl = imgUrl;
		this.date = date;
	}
	
	public ProductDTO(Product product) {
		this.id = product.getId();
		this.name = product.getName();
		this.description = product.getDescription();
		this.price = product.getPrice();
		this.date = product.getDate();
		this.imgUrl = product.getImgUrl();
	}
	
	public ProductDTO(Product product, Set<Category> categories) {
		this(product);
		categories.forEach(cat -> this.categories.add(new CategoryDTO(cat)));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Instant getdate() {
		return date;
	}

	public void setdate(Instant date) {
		this.date = date;
	}

	public List<CategoryDTO> getCategories() {
		return categories;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductDTO other = (ProductDTO) obj;
		return Objects.equals(id, other.id);
	}
	
}










