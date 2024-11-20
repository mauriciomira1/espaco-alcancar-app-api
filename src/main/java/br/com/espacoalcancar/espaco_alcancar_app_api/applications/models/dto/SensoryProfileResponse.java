package br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.dto;

import lombok.Data;

@Data
public class SensoryProfileResponse {

  private Integer id;
  private String childName;
  private String profileType;
  private String status;
  private String createdAt;
  private String updatedAt;
  private String resultsOfSensoryProfile;
}
