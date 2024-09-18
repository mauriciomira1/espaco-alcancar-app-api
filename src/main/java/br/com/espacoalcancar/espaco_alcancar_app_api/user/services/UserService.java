package br.com.espacoalcancar.espaco_alcancar_app_api.user.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.UserResponse;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.entities.UserEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.repositories.UserRepository;

public class UserService {

  @Autowired
  private UserRepository repository;

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

}
