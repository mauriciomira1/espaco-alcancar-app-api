package br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class SensoryProfileResponse {

  private UUID id;
  private String childName;
  private UUID childId;
  private String profileType;
  private String status;
  private String createdAt;
  private String updatedAt;
  private String resultsOfSensoryProfile;
}
