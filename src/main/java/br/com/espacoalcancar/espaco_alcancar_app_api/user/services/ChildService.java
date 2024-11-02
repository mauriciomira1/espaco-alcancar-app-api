package br.com.espacoalcancar.espaco_alcancar_app_api.user.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    ChildEntity childEntity = childRepository.findById(id).orElse(null);
    if (childEntity == null) {
      throw new IllegalArgumentException("Child not found with id: " + id);
    }
    return convertEntityToDto(childEntity);
  }

  // Buscar por todas as crianças baseado no ID do usuário
  public List<ChildResponse> list(Integer userId) {
    Iterable<ChildEntity> entity = childRepository.findAllByUserId(userId);
    List<ChildResponse> response = new ArrayList<>();
    for (ChildEntity entityChild : entity) {
      response.add(convertEntityToDto(entityChild));
    }
    return response;
  }

  // Buscar por todas as crianças cadastradas
  public List<ChildResponse> listAll() {
    // Obtendo o objeto de autenticação atual
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      throw new SecurityException("Usuário não autenticado");
    }

    // Verificando se o usuário possui as ROLES necessárias
    boolean hasAdminRole = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    boolean hasProfessionalRole = authentication.getAuthorities()
        .contains(new SimpleGrantedAuthority("ROLE_PROFESSIONAL"));

    if (!hasAdminRole && !hasProfessionalRole) {
      throw new SecurityException("Usuário não autorizado");
    }

    Iterable<ChildEntity> entity = childRepository.findAll();
    List<ChildResponse> response = new ArrayList<>();
    for (ChildEntity entityChild : entity) {
      response.add(convertEntityToDto(entityChild));
    }
    return response;
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
