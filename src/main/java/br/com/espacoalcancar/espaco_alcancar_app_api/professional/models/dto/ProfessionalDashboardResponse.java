package br.com.espacoalcancar.espaco_alcancar_app_api.professional.models.dto;

import java.time.LocalDate;
import java.util.UUID;

import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.ProfileType;
import lombok.Data;

@Data
public class ProfessionalDashboardResponse {
  private UUID id;
  private String name;
  private String phone;
  private String email;
  private String password;
  private boolean active;
  private LocalDate birth;
  private String registerNumber; // número de registro no conselho de saúde
  private String occupation; // "Fisioterapeuta", "Psicólogo", "Nutricionista", etc.
  private ProfileType profileType;
}
