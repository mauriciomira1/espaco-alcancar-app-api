package br.com.espacoalcancar.espaco_alcancar_app_api.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.ChildRequest;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.entities.ChildEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.repositories.ChildRepository;

@Service
public class ChildService {

  @Autowired
  ChildRepository repository;

  // Adicionar um filho
  public Integer create(ChildRequest request) {
    ChildEntity entity = new ChildEntity();

    entity.setBirth(request.getBirth());
    entity.setName(request.getName());
    entity.setGender(request.getGender());

    return repository.save(entity).getId();
  }

  public ChildEntity findById(Integer id) {
    return repository.findById(id).orElse(null);
  }

}
