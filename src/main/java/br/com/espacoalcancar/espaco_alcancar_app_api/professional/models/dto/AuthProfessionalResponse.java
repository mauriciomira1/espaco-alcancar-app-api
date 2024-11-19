package br.com.espacoalcancar.espaco_alcancar_app_api.professional.models.dto;

import java.util.List;

import lombok.Data;

@Data
public class AuthProfessionalResponse {

  private String token;
  private String refreshToken;
  private List<String> roles;
}
