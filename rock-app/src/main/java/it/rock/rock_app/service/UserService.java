package it.rock.rock_app.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.rock.rock_app.domain.Role;
import it.rock.rock_app.domain.User;
import it.rock.rock_app.model.UserDTO;
import it.rock.rock_app.repos.RoleRepository;
import it.rock.rock_app.repos.UserRepository;
import it.rock.rock_app.util.NotFoundException;
import it.rock.rock_app.util.UserAlreadyExistException;
import jakarta.transaction.Transactional;


@Transactional
@Service
public class UserService {

	@Autowired
	PasswordEncoder passwordEncoder;

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	public UserService(final UserRepository userRepository, final RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	public List<UserDTO> findAll() {
		final List<User> users = userRepository.findAll(Sort.by("id"));
		return users.stream()
				.map((user) -> mapToDTO(user, new UserDTO()))
				.toList();
	}

	public UserDTO get(final Long id) {
		return userRepository.findById(id)
				.map(user -> mapToDTO(user, new UserDTO()))
				.orElseThrow(NotFoundException::new);
	}

	public Long create(final UserDTO userDTO) {
		final User user = new User();
		mapToEntity(userDTO, user);
		return userRepository.save(user).getId();
	}

	public void update(final Long id, final UserDTO userDTO) {
		final User user = userRepository.findById(id)
				.orElseThrow(NotFoundException::new);
		mapToEntity(userDTO, user);
		userRepository.save(user);
	}

	public void delete(final Long id) {
		userRepository.deleteById(id);
	}

	private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
		userDTO.setId(user.getId());
		userDTO.setUsername(user.getUsername());
		userDTO.setPassword(user.getPassword());
		userDTO.setEmail(user.getEmail());
		userDTO.setFirstName(user.getFirstName());
		userDTO.setLastName(user.getLastName());
		userDTO.setMatchingPassword(user.getMatchingPassword());
		
		userDTO.setRoles(user.getRoles() == null ? null : user.getRoles().stream()
				.map(role -> role.getId())
				.toList());
		return userDTO;
	}

	private User mapToEntity(final UserDTO userDTO, final User user) {
		user.setUsername(userDTO.getUsername());
		user.setPassword(userDTO.getPassword());
		user.setEmail(userDTO.getEmail());
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		user.setMatchingPassword(userDTO.getMatchingPassword());
		user.setId(userDTO.getId());
		final List<Role> roles = roleRepository.findAllById(
				userDTO.getRoles() == null ? Collections.emptyList() : userDTO.getRoles());
		if (roles.size() != (userDTO.getRoles() == null ? 0 : userDTO.getRoles().size())) {
			throw new NotFoundException("one of roles not found");
		}
		user.setRoles(roles.stream().collect(Collectors.toSet()));
		return user;
	}

	public void register(UserDTO user) throws UserAlreadyExistException {

		//Let's check if user already registered with us
		if(checkIfUserExist(user.getEmail())){
			throw new UserAlreadyExistException("User already exists for this email");
		}
		User userEntity = new User();
		BeanUtils.copyProperties(user, userEntity);
		encodePassword(userEntity, user);
		userRepository.save(userEntity);
	}


	public boolean checkIfUserExist(String email) {
		return userRepository.findByEmail(email) !=null ? true : false;
	}

	private void encodePassword( User user, UserDTO userDTO){
		user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
	}


	private static final String USER_NOT_FOUND_MESSAGE = "User with username %s not found";


	public UserDTO loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		UserDTO dto = new UserDTO();
		BeanUtils.copyProperties(user, dto);
		return dto;
		}

	public UserDTO save(UserDTO userDTO) {
		User user =new User();
		BeanUtils.copyProperties(userDTO, user);
		userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		user=userRepository.save(user);
		BeanUtils.copyProperties(user, userDTO);
		return userDTO;
	}


	public UserDTO addRoleToUser(String username, String roleName) {
		User userEntity = userRepository.findByUsername(username);
		Role roleEntity = roleRepository.findByName(roleName);
		userEntity.getRoles().add(roleEntity);
		UserDTO userDTO= new UserDTO();
		BeanUtils.copyProperties(userEntity, userDTO);
		return userDTO;
	}

	public UserDTO findByUsername(String username) {
		User user= userRepository.findByUsername(username);
		UserDTO userDTO = new UserDTO();
		BeanUtils.copyProperties(user, userDTO);
		return userDTO;
	}
	
	public UserDTO findByUsernameAndPassword(String username, String password) {
		User user= userRepository.findByUsernameAndPassword(username,password);
		UserDTO userDTO = new UserDTO();
		BeanUtils.copyProperties(user, userDTO);
		return userDTO;
	}
	
}
