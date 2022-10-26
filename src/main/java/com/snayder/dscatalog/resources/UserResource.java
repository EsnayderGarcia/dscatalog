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

import com.snayder.dscatalog.dtos.UserDTO;
import com.snayder.dscatalog.dtos.UserInsertDTO;
import com.snayder.dscatalog.services.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/users")
public class UserResource {

    @Autowired
    private UserService userService;

    @GetMapping
    @ApiOperation("Busca paginada dos usuários")
    public ResponseEntity<Page<UserDTO>> findAll(
            @RequestParam(defaultValue = "0") @ApiParam(value = "Número da Página") int page,
            @RequestParam(defaultValue = "10") @ApiParam(value = "Produtos por Página") int size,
            @RequestParam(defaultValue = "ASC") @ApiParam(value = "Tipo da Ordenação") String direction,
            @RequestParam(defaultValue = "firstName") @ApiParam(value = "Informe por qual dado os us serão ordenados") String sort) {
        Pageable pageable = PageRequest.of(page, size, valueOf(direction.toUpperCase()), sort);
        Page<UserDTO> users = this.userService.findAllPaged(pageable);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{idUser}")
    @ApiOperation("Busca de um usuário por id")
    public ResponseEntity<UserDTO> findById(@PathVariable @ApiParam(value = "Id do Usuário", example = "1") Long idUser) {
    	UserDTO user = this.userService.findById(idUser);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    @ApiOperation("Inserção de um usuário")
    public ResponseEntity<UserDTO> insert(@Valid @RequestBody UserInsertDTO dto) {
        UserDTO userDTO = this.userService.insert(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userDTO.getId())
                .toUri();

        return ResponseEntity.created(uri).body(userDTO);
    }

    @PutMapping("/{idUser}")
    @ApiOperation("Atualização de um usuário")
    public ResponseEntity<UserDTO> update(
    		@Valid @RequestBody UserDTO dto,
            @PathVariable @ApiParam(value = "Id do usuário", example = "1") Long idUser) { 
        dto = this.userService.update(idUser, dto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{idUser}")
    @ApiOperation("Deleção de um user por id")
    public ResponseEntity<Void> delete(
    		@PathVariable @ApiParam(value = "Id do user", example = "1") Long idUser) {
        this.userService.delete(idUser);
        return ResponseEntity.noContent().build();
    }
}
