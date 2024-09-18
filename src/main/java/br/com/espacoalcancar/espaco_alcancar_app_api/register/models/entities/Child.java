package br.com.espacoalcancar.espaco_alcancar_app_api.register.models.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Entity
@Table(name = "tab_child")
public class Child {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(value = AccessLevel.NONE)
  private Integer id;

  @Column(nullable = false, length = 30)
  private String name;

  @Column(nullable = false)
  private Date birth;

  @Column(nullable = false)
  private Gender gender;

}
