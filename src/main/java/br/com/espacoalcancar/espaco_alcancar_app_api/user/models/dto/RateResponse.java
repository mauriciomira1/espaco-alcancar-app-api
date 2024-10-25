package br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class RateResponse {

  private Integer stars;
  private String comment;
  private Integer userId;
  private LocalDate createdAt;

}
