package br.com.espacoalcancar.espaco_alcancar_app_api.services.models.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

// Avaliação da clínica, pelo paciente

@Entity
@Data
public class Review {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(value = AccessLevel.NONE)
  private Integer id;

  @Column(length = 400)
  private String content;

  @Column(length = 1)
  private Integer stars;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}