package br.com.espacoalcancar.espaco_alcancar_app_api.professional.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.espacoalcancar.espaco_alcancar_app_api.professional.exceptions.ProfessionalAlreadyExists;
import br.com.espacoalcancar.espaco_alcancar_app_api.professional.models.dto.EditProfessionalDTO;
import br.com.espacoalcancar.espaco_alcancar_app_api.professional.models.dto.NewProfessionalDTO;
import br.com.espacoalcancar.espaco_alcancar_app_api.professional.models.entities.ProfessionalEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.professional.repositories.ProfessionalRepository;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.ProfileType;

@Service
public class ProfessionalService {

  @Autowired
  ProfessionalRepository professionalRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  // Adicionar um profissional
  public String addProfessional(NewProfessionalDTO newProfessional) {
    // Verificar se o email já está cadastrado
    if (professionalRepository.findByEmail(newProfessional.getEmail()) != null) {
      throw new ProfessionalAlreadyExists("Email já cadastrado");
    }

    newProfessional.setPassword(passwordEncoder.encode(newProfessional.getPassword()));

    professionalRepository.save(convertDtoToEntity(newProfessional));
    return "Profissional cadastrado com sucesso";
  }

  // Editar dados de um profissional
  public void updateProfessional(EditProfessionalDTO newProfessional) throws RuntimeException {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      throw new RuntimeException("User is not authenticated");
    }

    var principal = (ProfessionalEntity) authentication.getPrincipal();

    if (!(principal instanceof ProfessionalEntity)) {
      throw new RuntimeException("User principal is not of expected type");
    }

    Integer id = principal.getId();
    System.out.println("ID: " + id);

    ProfessionalEntity entity = professionalRepository.findById(id).get();
    entity.setName(newProfessional.getName());
    entity.setPhone(newProfessional.getPhone());
    entity.setBirth(newProfessional.getBirth());
    entity.setOccupation(newProfessional.getOccupation());
    entity.setRegisterNumber(newProfessional.getRegisterNumber());
    professionalRepository.save(entity);
  }

  // Arquivar um profissional
  public void archiveProfessional(Integer id) {
    ProfessionalEntity entity = professionalRepository.findById(id).get();
    entity.setActive(false);
    professionalRepository.save(entity);
  }

  // Desarquivar um profissional
  public void unarchiveProfessional(Integer id) {
    ProfessionalEntity entity = professionalRepository.findById(id).get();
    entity.setActive(true);
    professionalRepository.save(entity);
  }

  // Buscar um profissional
  public ProfessionalEntity getProfessional(Integer id) {
    return professionalRepository.findById(id).get();
  }

  // Listar todos os profissionais
  public List<ProfessionalEntity> listAll() {
    return professionalRepository.findAll();
  }

  // Listar todos os profissionais ativos
  public List<ProfessionalEntity> listAllActive() {
    return professionalRepository.findByActiveTrue();
  }

  // Obter profissional atualmente logado
  public ProfessionalEntity getCurrentProfessional() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !auth.isAuthenticated()) {
      throw new RuntimeException("User is not authenticated");
    }
    ProfessionalEntity principal = (ProfessionalEntity) auth.getPrincipal();
    return principal;
  }

  // Função acessória p/ converter NewProfessionalDTO em entidade
  private ProfessionalEntity convertDtoToEntity(NewProfessionalDTO newProfessional) {
    ProfessionalEntity entity = new ProfessionalEntity();
    entity.setName(newProfessional.getName());
    entity.setEmail(newProfessional.getEmail());
    entity.setPassword(newProfessional.getPassword());
    entity.setRegisterNumber(newProfessional.getRegisterNumber());
    entity.setOccupation(newProfessional.getOccupation());
    entity.setBirth(newProfessional.getBirth());
    entity.setActive(true);
    entity.setCreatedAt(LocalDateTime.now());
    entity.setProfileType(new ProfileType(false, true, false));
    return entity;
  }

  // Função acessória para obter os papéis do usuário
  public List<String> getUserRoles(String email) {
    ProfessionalEntity professional = professionalRepository.findByEmail(email);
    if (professional == null) {
      throw new UsernameNotFoundException("User not autenticated");
    } else {

      List<String> roles = new ArrayList<>();

      if (professional.getProfileType().isAdmin()) {
        roles.add("ADMIN");
      }
      if (professional.getProfileType().isProfessional()) {
        roles.add("PROFESSIONAL");
      }
      if (professional.getProfileType().isPatient()) {
        roles.add("PATIENT");
      }
      return roles;
    }
  }

  // Listar todos os perfis sensoriais de um profissional

}
