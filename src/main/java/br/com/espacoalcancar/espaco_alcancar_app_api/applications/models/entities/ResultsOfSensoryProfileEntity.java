package br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "tab_results_of_sensory_profile")
public class ResultsOfSensoryProfileEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ElementCollection
  private List<Integer> answers;

  @ManyToOne
  @JoinColumn(name = "sensory_profile_id")
  private SensoryProfileEntity sensoryProfile;
}
