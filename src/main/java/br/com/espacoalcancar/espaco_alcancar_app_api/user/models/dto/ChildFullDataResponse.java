package br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities.SensoryProfileEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.Gender;
import lombok.Data;

@Data
public class ChildFullDataResponse {
  private UUID id;
  private String name;
  private LocalDate birth;
  private Set<SensoryProfileEntity> sensoryProfiles;
  private Gender gender;
  private LocalDate createdAt;
  private LocalDate updatedAt;
}
