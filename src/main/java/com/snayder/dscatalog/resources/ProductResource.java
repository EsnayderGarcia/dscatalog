package com.snayder.dscatalog.resources;

import com.snayder.dscatalog.dtos.ProductDTO;
import com.snayder.dscatalog.services.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static org.springframework.data.domain.Sort.Direction.valueOf;

@RestController
@RequestMapping("/products")
public class ProductResource {

    @Autowired
    private ProductService productService;

    @GetMapping
    @ApiOperation("Busca paginada dos produtos")
    public ResponseEntity<Page<ProductDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ASC") String direction,
            @RequestParam(defaultValue = "name") String sort) {
        Pageable pageable = PageRequest.of(page, size, valueOf(direction.toUpperCase()), sort);
        Page<ProductDTO> products = this.productService.findAllPaged(pageable);

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{idProduct}")
    @ApiOperation("Busca de um produto por id")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long idProduct) {
        ProductDTO product = this.productService.findById(idProduct);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    @ApiOperation("Inserção de um produto")
    public ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO dto) {
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
    public ResponseEntity<ProductDTO> update(@RequestBody ProductDTO dto,
                                             @PathVariable Long idProduct) {
        dto = this.productService.update(idProduct, dto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{idProduct}")
    @ApiOperation("Deleção de um produto por id")
    public ResponseEntity<Void> delete(@PathVariable Long idProduct) {
        this.productService.delete(idProduct);
        return ResponseEntity.noContent().build();
    }
}
