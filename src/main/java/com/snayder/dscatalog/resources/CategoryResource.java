package com.snayder.dscatalog.resources;

import com.snayder.dscatalog.dtos.CategoryDTO;
import com.snayder.dscatalog.services.CategoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryResource {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    @ApiOperation("Busca todas as categorias cadastradas")
    public ResponseEntity<List<CategoryDTO>> findAll() {
        List<CategoryDTO> categories = this.categoryService.findAll();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{idCategory}")
    @ApiOperation("Busca de uma categoria por id")
    public ResponseEntity<CategoryDTO> findById(
    		@PathVariable @ApiParam(value = "Id da Categoria", example = "1") Long idCategory) {
        CategoryDTO category = this.categoryService.findById(idCategory);
        return ResponseEntity.ok(category);
    }

    @PostMapping
    @ApiOperation("Inserção de uma nova categoria")
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
    @ApiOperation("Atualização de uma categoria")
    public ResponseEntity<CategoryDTO> update(
    		  @RequestBody CategoryDTO dto,
              @PathVariable @ApiParam(value = "Id da Categoria", example = "1") Long idCategory) {
        dto = this.categoryService.update(idCategory, dto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{idCategory}")
    @ApiOperation("Exclusão de uma categoria por id")
    public ResponseEntity<Void> delete(
    		@PathVariable @ApiParam(value = "Id da Categoria", example = "1") Long idCategory) {
        this.categoryService.delete(idCategory);
        return ResponseEntity.noContent().build();
    }

}
