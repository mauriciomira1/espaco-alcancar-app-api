package br.com.espacoalcancar.espaco_alcancar_app_api.user.models.entities;

import java.util.List;

import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.Address;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.Gender;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.ProfileType;
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
public class UserEntity {

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

  @OneToMany(mappedBy = "userEntity")
  private List<ChildEntity> children;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  @Embedded
  private Address address;

  @Embedded
  private ProfileType profileType;

}
