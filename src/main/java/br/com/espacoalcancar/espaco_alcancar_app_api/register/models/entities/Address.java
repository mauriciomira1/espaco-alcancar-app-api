package br.com.espacoalcancar.espaco_alcancar_app_api.register.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Address {

  @Column(nullable = false, length = 50)
  private String address;

  @Column(nullable = false, length = 20)
  private String city;

  @Column(length = 60)
  private String complement;

}