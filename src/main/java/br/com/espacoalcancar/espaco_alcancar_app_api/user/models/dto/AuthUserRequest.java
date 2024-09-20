package br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthUserRequest {
  private String email;
  private String password;
}
