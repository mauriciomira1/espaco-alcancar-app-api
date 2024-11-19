package br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.dto;

import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities.SensoryProfileType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensoryProfileRequest {
  private SensoryProfileType profileType;
  private Integer childId;
}
