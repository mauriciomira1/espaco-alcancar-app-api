package br.com.espacoalcancar.espaco_alcancar_app_api.user.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.ChildRequest;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.ChildResponse;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.ChildUpdate;
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
  public UUID create(ChildRequest request, HttpServletRequest httpServletRequest) {
    // Recuperando o ID do usuário configurado no SecurityFilter.java
    var userIdObject = httpServletRequest.getAttribute("user_id");
    UUID userId = UUID.fromString(userIdObject.toString());

    UserEntity userEntity = userRepository.findById(userId)
        .orElseThrow(() -> new UsernameNotFoundException("Child not found with this id."));

    String firstName = request.getName().split(" ")[0];

    var childs = userEntity.getChildren();
    for (ChildEntity child : childs) {

      if (child.getName().contains(firstName)) {
        throw new IllegalArgumentException("Child already exists with name: " + firstName);
      }
    }

    ChildEntity entity = new ChildEntity();

    entity.setBirth(request.getBirth());
    entity.setName(request.getName());
    entity.setGender(request.getGender());
    entity.setUser(userEntity);

    return childRepository.save(entity).getId();
  }

  // Buscar por uma criança
  public ChildResponse findById(UUID id) {
    ChildEntity childEntity = childRepository.findById(id).orElse(null);
    if (childEntity == null) {
      throw new IllegalArgumentException("Child not found with id: " + id);
    }
    return convertEntityToDto(childEntity);
  }

  // Buscar por todas as crianças baseado no ID do usuário (requere
  // ROLE_PROFESSIONAL ou ROLE_ADMIN)
  public List<ChildResponse> list(UUID userId) {
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

  // Classe acessória para converter Entity em DTO
  private ChildResponse convertEntityToDto(ChildEntity entity) {
    ChildResponse response = new ChildResponse();
    response.setId(entity.getId());
    response.setName(entity.getName());
    response.setBirth(entity.getBirth());
    response.setGender(entity.getGender().toString());
    return response;
  }

  // Atualizar um filho
  public ChildResponse update(ChildUpdate request) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      throw new RuntimeException("User is not authenticated");
    }

    ChildEntity entity = childRepository.findById(request.getId())
        .orElseThrow(() -> new UsernameNotFoundException("Child not found with this id."));

    entity.setBirth(request.getBirth());
    entity.setName(request.getName());
    entity.setGender(request.getGender());

    childRepository.save(entity);

    return convertEntityToDto(entity);
  }

  // Remover um filho
  public void remove(UUID childId) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      throw new RuntimeException("User is not authenticated");
    }

    ChildEntity entity = childRepository.findById(childId)
        .orElseThrow(() -> new UsernameNotFoundException("Child not found with this id."));

    childRepository.delete(entity);
  }
}
