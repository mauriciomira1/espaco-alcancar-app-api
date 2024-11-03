package br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto;

import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.Address;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.ProfileType;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.Relationship;
import lombok.Data;

@Data
public class UserDashboardResponse {

  private Integer id;
  private String name;
  private String phone;
  private String email;
  private String password;
  private Relationship relationship;
  private Address address;
  private ProfileType profileType;

}
