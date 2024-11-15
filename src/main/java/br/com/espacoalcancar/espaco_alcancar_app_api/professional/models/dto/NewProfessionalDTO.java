package br.com.espacoalcancar.espaco_alcancar_app_api.professional.models.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewProfessionalDTO {

  private String name;
  private String email;
  private LocalDate birth;
  private String password;
  private String occupation; // Ocupação: "Fisioterapeuta", "Psicólogo", "Nutricionista", etc.
  private String registerNumber; // número de registro no conselho de saúde

}
