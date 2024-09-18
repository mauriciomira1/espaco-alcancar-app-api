package br.com.espacoalcancar.espaco_alcancar_app_api.register.models.entities;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Profile {

  private boolean patient;
  private boolean professional;
  private boolean admin;
}
