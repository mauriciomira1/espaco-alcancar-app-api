package br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensoryProfileRequest {
  private UUID childId;
}
