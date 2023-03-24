package com.snayder.dscatalog.resources;

import static org.springframework.data.domain.Sort.Direction.valueOf;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/products")
public class ProductResource {

    @Autowired
    private ProductService productService;

    @GetMapping
    @ApiOperation("Busca paginada dos produtos")
    public ResponseEntity<Page<ProductDTO>> findAll(
            @RequestParam(defaultValue = "") @ApiParam(value = "Nome do Produto") String name,
            @RequestParam(defaultValue = "0") @ApiParam(value = "Id da Categoria") long categoryId,
            @RequestParam(defaultValue = "0.0") @ApiParam(value = "Preço Mínimo do Produto") double minPrice,
            @RequestParam(defaultValue = "0.0") @ApiParam(value = "Preço Máximo do Produto") double maxPrice,
            @RequestParam(defaultValue = "0") @ApiParam(value = "Número da Página") int page,
            @RequestParam(defaultValue = "10") @ApiParam(value = "Produtos por Página") int size,
            @RequestParam(defaultValue = "ASC") @ApiParam(value = "Tipo da Ordenação") String direction,
            @RequestParam(defaultValue = "name") @ApiParam(value = "Informe por qual dado os produtos serão ordenados") String sort) {
        Pageable pageable = PageRequest.of(page, size, valueOf(direction.toUpperCase()), sort);
        Page<ProductDTO> products = this.productService.findAllPaged(name, categoryId, minPrice, maxPrice, pageable);

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{idProduct}")
    @ApiOperation("Busca de um produto por id")
    public ResponseEntity<ProductDTO> findById(@PathVariable @ApiParam(value = "Id do Produto", example = "1") Long idProduct) {
        ProductDTO product = this.productService.findById(idProduct);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    @ApiOperation("Inserção de um produto")
    public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO dto) {
        dto = this.productService.insert(dto);

        /*Uri de acesso ao novo produto criado!*/
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.getId())
                .toUri();

        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping("/{idProduct}")
    @ApiOperation("Atualização de um produto")
    public ResponseEntity<ProductDTO> update(
    		@Valid @RequestBody ProductDTO dto,
            @PathVariable @ApiParam(value = "Id do Produto", example = "1") Long idProduct) { 
        dto = this.productService.update(idProduct, dto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{idProduct}")
    @ApiOperation("Deleção de um produto por id")
    public ResponseEntity<Void> delete(
    		@PathVariable @ApiParam(value = "Id do Produto", example = "1") Long idProduct) {
        this.productService.delete(idProduct);
        return ResponseEntity.noContent().build();
    }
}
