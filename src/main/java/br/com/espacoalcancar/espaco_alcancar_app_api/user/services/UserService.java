package br.com.espacoalcancar.espaco_alcancar_app_api.user.services;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NameNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
  public UserDashboardResponse findById(Integer id) throws UsernameNotFoundException {
    var response = this.repository.findById(id)
        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
    return convertToUserDashboardResponse(response);
  }

  // Criar novo usuário
  public Integer create(UserRequest request) {
    UserEntity entity = new UserEntity();
    entity.setName(request.getName());
    entity.setAddress(request.getAddress());
    entity.setEmail(request.getEmail());
    entity.setRelationship(request.getRelationship());
    entity.setPhone(request.getPhone());
    entity.setProfileType(request.getProfileType());
    entity.setPassword(passwordEncoder.encode(request.getPassword()));
    return repository.save(entity).getId();
  }

  // Atualizar usuário existente
  public UserDashboardResponse updateUser(Integer userId, UserRequest request) {
    UserEntity userEntity = repository.findById(userId)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    BeanUtils.copyProperties(request, userEntity, "id", "password");
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
    // Obtendo o objeto de autenticação atual
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      throw new RuntimeException("User is not authenticated");
    }

    var principal = (UserDashboardResponse) authentication.getPrincipal();
    if (!(principal instanceof UserDashboardResponse)) {
      throw new RuntimeException("User principal is not of expected type");
    }

    var userId = principal.getId();
    return findById(userId);
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
    response.setChildren(userEntity.getChildren());
    response.setProfileType(userEntity.getProfileType());
    response.setRelationship(userEntity.getRelationship());
    response.setPassword(userEntity.getPassword());
    return response;
  }
}