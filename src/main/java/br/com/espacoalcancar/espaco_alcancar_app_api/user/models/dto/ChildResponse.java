package br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Data;

@Data
public class ChildResponse {
  private UUID id;
  private String name;
  private LocalDate birth;
  private String gender;
}
