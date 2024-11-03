package br.com.espacoalcancar.espaco_alcancar_app_api.user.models;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileType {

  private boolean patient;
  private boolean professional;
  private boolean admin;
}
