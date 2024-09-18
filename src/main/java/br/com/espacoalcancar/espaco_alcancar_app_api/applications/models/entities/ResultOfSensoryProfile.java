package br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class ResultOfSensoryProfile {

  private String title;
  private int value;
  private int maxValue;

}
