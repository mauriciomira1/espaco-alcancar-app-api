package br.com.espacoalcancar.espaco_alcancar_app_api.user.models.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.entities.SensoryProfileEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
  @JoinColumn(name = "user_id", nullable = false)
  @JsonBackReference
  private UserEntity user;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @OneToMany(mappedBy = "child", fetch = FetchType.LAZY)

  private Set<SensoryProfileEntity> sensoryProfiles = new HashSet<>();

  @Enumerated(EnumType.STRING)
  private Gender gender;

  @Column(name = "user_id", insertable = false, updatable = false)
  private Integer userId;

  @Column(nullable = false, updatable = false, name = "created_at")
  private LocalDateTime createdAt = LocalDateTime.now();

  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, name = "updated_at")
  private LocalDateTime updatedAt = LocalDateTime.now();

}
