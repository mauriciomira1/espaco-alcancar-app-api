package br.com.espacoalcancar.espaco_alcancar_app_api.services.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

// Pergunta do Perfil Sensorial

@Embeddable
@Data
public class Question {

  @Column(length = 2)
  private Integer number;

  @Column(length = 200)
  private String question;

  @Enumerated(EnumType.STRING)
  private Answer answer;

  private boolean filled;
}
