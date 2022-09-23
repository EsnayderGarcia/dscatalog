package com.snayder.dscatalog.resources;

import com.snayder.dscatalog.dtos.CategoryDTO;
import com.snayder.dscatalog.services.CategoryService;
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
    public ResponseEntity<List<CategoryDTO>> findAll() {
        List<CategoryDTO> categories = this.categoryService.findAll();
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
