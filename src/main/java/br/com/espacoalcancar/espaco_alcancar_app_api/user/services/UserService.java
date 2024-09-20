package br.com.espacoalcancar.espaco_alcancar_app_api.user.services;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NameNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

  // Criar novo usuário
  public Integer create(UserRequest request) {
    UserEntity entity = new UserEntity();

    entity.setName(request.getName());
    entity.setAddress(request.getAddress());
    entity.setEmail(request.getEmail());
    entity.setGender(request.getGender());
    entity.setPhone(request.getPhone());
    entity.setProfileType(request.getProfileType());
    entity.setPassword(passwordEncoder.encode(request.getPassword()));

    return repository.save(entity).getId();
  }

  // Classe acessória para conversão de UserEntity para UserResponse
  private UserResponse convertToUserResponse(UserEntity userEntity) {
    UserResponse response = new UserResponse();
    response.setId(userEntity.getId());
    response.setName(userEntity.getName());
    return response;
  }
}
