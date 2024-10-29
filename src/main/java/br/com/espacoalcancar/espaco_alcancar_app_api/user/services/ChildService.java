package br.com.espacoalcancar.espaco_alcancar_app_api.user.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.ChildRequest;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.ChildResponse;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.entities.ChildEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.entities.UserEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.repositories.ChildRepository;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class ChildService {

  @Autowired
  ChildRepository childRepository;

  @Autowired
  UserRepository userRepository;

  // Adicionar um filho
  public Integer create(ChildRequest request, HttpServletRequest httpServletRequest) {

    // Recuperando o ID do usuário configurado no SecurityFilter.java
    var userIdObject = httpServletRequest.getAttribute("user_id");
    Integer userId = Integer.valueOf(userIdObject.toString());

    UserEntity userEntity = userRepository.findById(userId).orElse(null);

    ChildEntity entity = new ChildEntity();

    entity.setBirth(request.getBirth());
    entity.setName(request.getName());
    entity.setGender(request.getGender());
    entity.setUser(userEntity);

    return childRepository.save(entity).getId();
  }

  // Buscar por uma criança
  public ChildResponse findById(Integer id) {
    /*
     * return childRepository.findById(id)
     * .map(entity -> new ChildResponse(entity.getId(), entity.getName(),
     * entity.getBirth(), entity.getGender(),
     * entity.getUser().getId()))
     * .orElse(null);
     */
    var childEntity = childRepository.findById(id).orElse(null);
    return convertEntityToDto(childEntity);

  }

  // Buscar por todas as crianças baseado no ID do usuário
  public Iterable<ChildResponse> listAll(Integer userId) {
    Iterable<ChildEntity> entity = childRepository.findAllByUserId(userId);

  }

  // Buscar por todas as crianças cadastradas
  public Iterable<ChildResponse> listAll() {
    return childRepository.findAll();
  }

  private ChildResponse convertEntityToDto(ChildEntity entity) {
    ChildResponse response = new ChildResponse();
    response.setId(entity.getId());
    response.setName(entity.getName());
    response.setBirth(entity.getBirth());
    response.setGender(entity.getGender().toString());
    return response;
  }

}
