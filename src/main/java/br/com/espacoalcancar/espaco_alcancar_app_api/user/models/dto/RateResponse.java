package br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class RateResponse {
  private Integer stars;
  private String comment;
  private UUID userId;
  private LocalDateTime createdAt;
}
