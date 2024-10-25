package br.com.espacoalcancar.espaco_alcancar_app_api.user.models.entities;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tab_rate")
public class RateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(value = AccessLevel.NONE)
  private Integer id;

  @Min(1)
  @Max(5)
  private Integer stars;

  @Length(max = 300)
  private String comment;

  private Integer userId;

  private LocalDateTime createdAt;
}
