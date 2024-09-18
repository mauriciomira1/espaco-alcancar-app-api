package br.com.espacoalcancar.espaco_alcancar_app_api.register.models.entities;

import java.time.LocalDate;

import br.com.espacoalcancar.espaco_alcancar_app_api.register.models.Gender;
import br.com.espacoalcancar.espaco_alcancar_app_api.services.models.entities.SensoryProfile;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Entity
@Table(name = "tab_child")
public class ChildEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(value = AccessLevel.NONE)
  private Integer id;

  @Column(length = 30)
  private String name;

  private LocalDate birth;

  @ManyToOne
  private RegisterEntity registerEntity;

  @OneToMany(mappedBy = "child")
  private SensoryProfile sensoryProfile;

  @Enumerated(EnumType.STRING)
  private Gender gender;

}
