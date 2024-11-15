package br.com.espacoalcancar.espaco_alcancar_app_api.professional.services;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.espacoalcancar.espaco_alcancar_app_api.professional.models.dto.NewProfessionalDTO;
import br.com.espacoalcancar.espaco_alcancar_app_api.professional.models.entities.ProfessionalEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.professional.repositories.ProfessionalRepository;

@Service
public class ProfessionalService {

  @Autowired
  ProfessionalRepository professionalRepository;

  // Adicionar um profissional
  public Integer addProfessional(NewProfessionalDTO newProfessional) {
    return professionalRepository.save(convertDtoToEntity(newProfessional)).getId();
  }

  // Arquivar um profissional
  public void archiveProfessional(Integer id) {
    ProfessionalEntity entity = professionalRepository.findById(id).get();
    entity.setActive(false);
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

  // Editar dados de um profissional
  public void updateProfessional(Integer id, NewProfessionalDTO newProfessional) {
    LocalDate birth = newProfessional.getBirth();

    ProfessionalEntity entity = professionalRepository.findById(id).get();
    entity.setName(newProfessional.getName());
    entity.setEmail(newProfessional.getEmail());
    entity.setPassword(newProfessional.getPassword());
    entity.setRegisterNumber(newProfessional.getRegisterNumber());
    entity.setOccupation(newProfessional.getOccupation());
    entity.setBirth(newProfessional.getBirth());
    professionalRepository.save(entity);
  }

  // Função acessória p/ converter DTO em entidade
  private ProfessionalEntity convertDtoToEntity(NewProfessionalDTO newProfessional) {
    ProfessionalEntity entity = new ProfessionalEntity();
    entity.setName(newProfessional.getName());
    entity.setEmail(newProfessional.getEmail());
    entity.setPassword(newProfessional.getPassword());
    entity.setRegisterNumber(newProfessional.getRegisterNumber());
    entity.setOccupation(newProfessional.getOccupation());
    entity.setBirth(newProfessional.getBirth());
    entity.setActive(true);
    return entity;
  }

}
