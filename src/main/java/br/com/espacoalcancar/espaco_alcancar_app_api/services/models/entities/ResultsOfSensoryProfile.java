package br.com.espacoalcancar.espaco_alcancar_app_api.services.models.entities;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class ResultsOfSensoryProfile {

  @ElementCollection
  private List<ResultOfSensoryProfile> result;

}
