package br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto;

import org.hibernate.validator.constraints.Length;

import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.Address;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.Gender;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.ProfileType;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserRequest {

  @Length(min = 8, max = 70, message = "O campo [nome] deve ter pelo menos 8 caracteres.")
  private String name;

  @Length(min = 6, max = 100, message = "O campo [senha] deve conter entre 6 e 20 caracteres.")
  private String password;
  private String phone;

  @Email(message = "O campo [email] deve conter um e-mail válido.")
  private String email;
  private Gender gender;
  private Address address;
  private ProfileType profileType;
}
