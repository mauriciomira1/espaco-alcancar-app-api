package br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class UserResponse {
  private UUID id;
  private String name;
}
