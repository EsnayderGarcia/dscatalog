package com.snayder.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snayder.dscatalog.dtos.UserDTO;
import com.snayder.dscatalog.dtos.UserInsertDTO;
import com.snayder.dscatalog.dtos.UserUpdateDTO;
import com.snayder.dscatalog.entities.Role;
import com.snayder.dscatalog.entities.User;
import com.snayder.dscatalog.repositories.UserRepository;
import com.snayder.dscatalog.services.exceptions.DataBaseException;
import com.snayder.dscatalog.services.exceptions.ResourceNotFoundException;

/*Essa anotação vai registrar esta classe como um componente*/ 
/*que vai participar do sistema de injeção de dependência do spring*/
@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	private static Logger LOGGER = LoggerFactory.getLogger(UserService.class);
	
	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		User user = userRepository.findById(id)
						.orElseThrow(() -> new ResourceNotFoundException("Usuário não Encontrado!"));

		return new UserDTO(user);
	}
	
	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(Pageable pageable) {
		/*O Pageable já é um Stream*/
		Page<User> users = userRepository.findAll(pageable);
		return users.map(UserDTO::new);
	}

	@Transactional
	public UserDTO insert(UserInsertDTO dto) {
		User user = new User();

		convertToUser(dto, user);
		user.setPassword(encoder.encode(dto.getPassword()));
		user = userRepository.save(user);
		
		return new UserDTO(user);
	}
	
	@Transactional
	public UserDTO update(Long idUser, UserUpdateDTO dto) {
		try {
			User user = userRepository.getOne(idUser);
			
			user.getRoles().clear();
			convertToUser(dto, user);
	
			user = userRepository.save(user);
			
			return new UserDTO(user);
		} 
		catch (EntityNotFoundException ex) {
			throw new ResourceNotFoundException("Usuário não encontrado para atualização");
		}
	}

	public void delete(Long idUser) {
		try {
			userRepository.deleteById(idUser);
		} 
		catch (EmptyResultDataAccessException ex) {
			throw new ResourceNotFoundException("Usuário não encontrado para exclusão");
		}
		catch(DataIntegrityViolationException ex) {
			throw new DataBaseException("Está operação viola a integridade do Banco de Dados");
		}
	}
	
	private void convertToUser(UserDTO dto, User user) {
		user.setFirstName(dto.getFirstName());
		user.setLastName(dto.getLastName());
		user.setEmail(dto.getEmail());
		
		dto.getRoles().forEach(r -> user.getRoles().add(new Role(r)));
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> optUser = userRepository.findByEmail(username);
		
		if(optUser.isEmpty()) {
			LOGGER.error("Usuário não encontrado: "+username);
			throw new UsernameNotFoundException("Email não encontrado");
		}
		
		LOGGER.info("Usuário encontrado: "+username);
		return optUser.get();
	}

}


