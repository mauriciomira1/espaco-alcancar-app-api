package br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto;

import lombok.Data;

@Data
public class RateRequest {

  private Integer stars;
  private String comment;

}
