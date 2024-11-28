package br.com.espacoalcancar.espaco_alcancar_app_api.professional.models.dto;

import java.time.LocalDate;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EditProfessionalDTO {

  @Column(length = 40)
  private String name;

  @Column(length = 16)
  private String phone;

  @Column(length = 12)
  private LocalDate birth;

  @Column(length = 24)
  private String occupation; // Ocupação: "Fisioterapeuta", "Psicólogo", "Nutricionista", etc.

  @Column(length = 16)
  private String registerNumber; // número de registro no conselho de saúde
}
