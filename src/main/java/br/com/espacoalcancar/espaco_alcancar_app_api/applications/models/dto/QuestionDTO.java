package br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO {

  private Integer number;
  private String question;

}
