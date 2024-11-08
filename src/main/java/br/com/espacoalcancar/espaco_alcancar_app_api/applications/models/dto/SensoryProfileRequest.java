package br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.dto;

import java.util.List;

import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities.SensoryProfileType;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensoryProfileRequest {

  private Status status;
  private SensoryProfileType profileType;
  private Integer childId;
  private List<Integer> answers;

}
