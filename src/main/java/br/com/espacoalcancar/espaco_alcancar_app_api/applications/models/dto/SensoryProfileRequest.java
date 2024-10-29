package br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.dto;

import java.util.List;

import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities.Question;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities.ResultsOfSensoryProfile;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities.SensoryProfileType;
import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities.Status;
import lombok.Data;

@Data
public class SensoryProfileRequest {

  private Status status;
  private SensoryProfileType profileType;
  private List<Question> questions;
  private ResultsOfSensoryProfile resultsOfSensoryProfile;
  private Integer childId;

}
