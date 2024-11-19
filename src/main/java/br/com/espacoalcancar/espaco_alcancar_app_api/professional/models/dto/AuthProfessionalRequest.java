package br.com.espacoalcancar.espaco_alcancar_app_api.professional.models.dto;

import lombok.Data;

@Data
public class AuthProfessionalRequest {

  private String email;
  private String password;

}
