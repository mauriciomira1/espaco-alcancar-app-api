package br.com.espacoalcancar.espaco_alcancar_app_api.professional.models.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.ProfileType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Entity
@Data
@Table(name = "tab_professional")
public class ProfessionalEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(value = AccessLevel.NONE)
  private Integer id;

  @Column(length = 70, nullable = false, unique = true)
  private String name;

  @Column(length = 16)
  private String phone;

  @Column(length = 40, nullable = false, unique = true)
  private String email;

  @Column(length = 100, nullable = false)
  private String password;

  @Column(nullable = false)
  private boolean active; // Para desativar um profissional

  @Temporal(TemporalType.DATE)
  private LocalDate birth;

  @Column(length = 14)
  private String registerNumber; // número de registro no conselho de saúde

  @Column(length = 30, nullable = false)
  private String occupation; // "Fisioterapeuta", "Psicólogo", "Nutricionista", etc.

  @Embedded
  private ProfileType profileType;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;
}
