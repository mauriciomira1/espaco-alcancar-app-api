package br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto;

import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.validator.constraints.Length;

import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.Gender;
import lombok.Data;

@Data
public class ChildUpdate {
  private UUID id;
  @Length(min = 8, max = 70, message = "O campo [nome] deve ter pelo menos 8 caracteres.")
  private String name;
  private LocalDate birth;
  private Gender gender;
}
