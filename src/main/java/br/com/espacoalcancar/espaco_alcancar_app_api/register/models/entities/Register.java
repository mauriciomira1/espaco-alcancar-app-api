package br.com.espacoalcancar.espaco_alcancar_app_api.register.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Entity
@Data
@Table(name = "tab_user")
public class Register {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(value = AccessLevel.NONE)
  private Integer id;

  @Column(nullable = false, length = 70)
  private String fullname;

  @Column(nullable = false, length = 16)
  private String phone;

  @Column(nullable = false, length = 30)
  private String email;

  @OneToMany
  private Child child;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  @Embedded
  private Address address;

  @Embedded
  private Profile profile;

}
