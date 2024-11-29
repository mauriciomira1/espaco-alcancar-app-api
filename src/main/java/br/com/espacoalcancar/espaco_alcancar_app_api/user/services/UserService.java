package br.com.espacoalcancar.espaco_alcancar_app_api.user.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.naming.NameNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.ProfileType;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.UserDashboardResponse;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.UserRequest;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.UserResponse;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.entities.UserEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.repositories.UserRepository;

@Service
public class UserService {

  @Autowired
  private UserRepository repository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  // Obter os papéis do usuário
  public List<String> getUserRoles(String email) {
    Optional<UserEntity> user = repository.findByEmail(email);
    if (user.isEmpty()) {
      throw new UsernameNotFoundException("User not autenticated");
    } else {

      List<String> roles = new ArrayList<>();

      if (user.get().getProfileType().isAdmin()) {
        roles.add("ADMIN");
      }
      if (user.get().getProfileType().isProfessional()) {
        roles.add("PROFESSIONAL");
      }
      if (user.get().getProfileType().isPatient()) {
        roles.add("PATIENT");
      }
      return roles;
    }
  }

  // Listar todos os usuários cadastrados
  public List<UserResponse> listAll() {
    List<UserEntity> users = repository.findAll();
    List<UserResponse> response = new ArrayList<>();
    for (UserEntity user : users) {
      UserResponse res = new UserResponse();
      BeanUtils.copyProperties(user, res);
      response.add(res);
    }
    return response;
  }

  // Procurar por um usuário a partir do nome
  public UserResponse findByName(String name) throws NameNotFoundException {
    UserResponse response = this.repository.findByName(name)
        .map(this::convertToUserResponse)
        .orElseThrow(() -> new NameNotFoundException("Usuário não encontrado."));
    return response;
  }

  // Procurar por um usuário a partir do ID
  public UserDashboardResponse findById(UUID id) throws UsernameNotFoundException {
    var response = this.repository.findById(id)
        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
    return convertToUserDashboardResponse(response);
  }

  // Criar novo usuário
  public UUID create(UserRequest request) {
    UserEntity entity = new UserEntity();

    request.setProfileType(new ProfileType(true, false, false));

    entity.setName(request.getName());
    entity.setAddress(request.getAddress());
    entity.setEmail(request.getEmail());
    entity.setRelationship(request.getRelationship());
    entity.setPhone(request.getPhone());
    entity.setProfileType(request.getProfileType());
    entity.setPassword(passwordEncoder.encode(request.getPassword()));
    return repository.save(entity).getId();
  }

  // Editar dados do usuário
  public UserDashboardResponse updateUser(UUID userId, UserRequest request) {
    UserEntity userEntity = repository.findById(userId)
        .orElseThrow(() -> new UsernameNotFoundException("User not autenticated"));

    BeanUtils.copyProperties(request, userEntity, "id", "password", "profileType", "email");
    if (request.getPassword() != null && !request.getPassword().isEmpty()) {
      userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
    }

    repository.save(userEntity);

    UserDashboardResponse response = new UserDashboardResponse();
    BeanUtils.copyProperties(userEntity, response);
    return response;
  }

  // Obter usuário atual
  public UserDashboardResponse getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new RuntimeException("User is not authenticated");
    }

    UserDashboardResponse principal = (UserDashboardResponse) authentication.getPrincipal();
    if (!(principal instanceof UserDashboardResponse)) {
      throw new RuntimeException("User principal is not of expected type");
    }

    return principal;
  }

  // Classe acessória para conversão de UserEntity para UserResponse
  private UserResponse convertToUserResponse(UserEntity userEntity) {
    UserResponse response = new UserResponse();
    response.setId(userEntity.getId());
    response.setName(userEntity.getName());
    return response;
  }

  // Classe acessória para conversão de UserEntity para UserDashboardResponse
  private UserDashboardResponse convertToUserDashboardResponse(UserEntity userEntity) {
    UserDashboardResponse response = new UserDashboardResponse();
    response.setId(userEntity.getId());
    response.setName(userEntity.getName());
    response.setAddress(userEntity.getAddress());
    response.setEmail(userEntity.getEmail());
    response.setPhone(userEntity.getPhone());
    response.setProfileType(userEntity.getProfileType());
    response.setRelationship(userEntity.getRelationship());
    response.setPassword(userEntity.getPassword());
    return response;
  }
}