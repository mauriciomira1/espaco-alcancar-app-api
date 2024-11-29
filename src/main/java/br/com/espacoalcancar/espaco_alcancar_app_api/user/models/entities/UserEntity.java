package br.com.espacoalcancar.espaco_alcancar_app_api.user.models.entities;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.Date;

import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.Address;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.ProfileType;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.Relationship;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Entity
@Data
@Table(name = "tab_user")
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Setter(value = AccessLevel.NONE)
  private UUID id;

  @Column(length = 70, nullable = false, unique = true)
  private String name;

  @Column(length = 16)
  private String phone;

  @Column(length = 40, nullable = false, unique = true)
  private String email;

  @Column(length = 100, nullable = false)
  private String password;

  @OneToMany(mappedBy = "user")
  @JsonManagedReference
  private List<ChildEntity> children;

  @Enumerated(EnumType.STRING)
  private Relationship relationship;

  @Embedded
  private Address address;

  @Embedded
  private ProfileType profileType;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)
  private Date createdAt = new Date();
}
