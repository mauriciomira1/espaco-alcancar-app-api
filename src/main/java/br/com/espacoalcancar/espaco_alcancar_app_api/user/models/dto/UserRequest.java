package br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto;

import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.Address;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.Gender;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.ProfileType;
import lombok.Data;

@Data
public class UserRequest {

  private String name;
  private String password;
  private String phone;
  private String email;
  private Gender gender;
  private Address address;
  private ProfileType profileType;
}
