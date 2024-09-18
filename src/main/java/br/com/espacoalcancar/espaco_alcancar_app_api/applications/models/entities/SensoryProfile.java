package br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities;

import java.time.LocalDateTime;
import java.util.List;

import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.entities.ChildEntity;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

// Perfil Sensorial

@Entity
@Data
public class SensoryProfile {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(value = AccessLevel.NONE)
  private Integer id;

  @Enumerated(EnumType.STRING)
  private Status status;

  @Enumerated(EnumType.STRING)
  private SensoryProfileType profileType;

  @ElementCollection
  private List<Question> questions;

  @Embedded
  private ResultsOfSensoryProfile resultsOfSensoryProfile;

  @ManyToOne(optional = false)
  private ChildEntity child;

  private LocalDateTime createdAt;
}