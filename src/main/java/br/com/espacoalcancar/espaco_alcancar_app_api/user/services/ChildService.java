package br.com.espacoalcancar.espaco_alcancar_app_api.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.ChildRequest;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.entities.ChildEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.entities.UserEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.repositories.ChildRepository;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class ChildService {

  @Autowired
  ChildRepository repository;

  @Autowired
  UserRepository userRepository;

  // Adicionar um filho
  public Integer create(ChildRequest request, HttpServletRequest httpServletRequest) {
    var userIdObject = httpServletRequest.getAttribute("user_id");
    Integer userId = Integer.valueOf(userIdObject.toString());

    System.out.println("----------------------------------------------------------------");
    System.out.println(userId);

    UserEntity userEntity = userRepository.findById(userId).orElse(null);

    ChildEntity entity = new ChildEntity();

    entity.setBirth(request.getBirth());
    entity.setName(request.getName());
    entity.setGender(request.getGender());
    entity.setUserEntity(userEntity); // id do User

    return repository.save(entity).getId();
  }

  public ChildEntity findById(Integer id) {
    return repository.findById(id).orElse(null);
  }

}
