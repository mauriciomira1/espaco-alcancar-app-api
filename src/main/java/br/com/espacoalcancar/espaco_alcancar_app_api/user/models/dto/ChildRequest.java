package br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto;

import java.time.LocalDate;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.Gender;
import lombok.Data;

@Data
public class ChildRequest {
  private String name;
  private LocalDate birth;
  private Gender gender;
}
