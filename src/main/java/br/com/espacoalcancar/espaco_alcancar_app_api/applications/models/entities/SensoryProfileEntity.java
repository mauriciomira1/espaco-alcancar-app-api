package br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities;

import java.time.LocalDateTime;
import java.util.List;

import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.entities.ChildEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

// Perfil Sensorial

@Entity
@Data
@Table(name = "tab_sensory_profile")
public class SensoryProfileEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(value = AccessLevel.NONE)
  private Integer id;

  @Enumerated(EnumType.STRING)
  private Status status;

  @Enumerated(EnumType.STRING)
  private SensoryProfileType profileType;

  @OneToMany(mappedBy = "resultsOfSensoryProfile")
  private List<ResultsOfSensoryProfile> resultsOfSensoryProfile;

  @ManyToOne(optional = false)
  @JoinColumn(name = "child_id")
  private ChildEntity child;

  private LocalDateTime createdAt;
}