package br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto;

import java.util.List;
import lombok.Data;

@Data
public class AuthUserResponse {

  private String token;
  private List<String> roles;

}
