package br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResultsRequestDTO {

  private Integer sensoryProfileId;

  @Size(max = 86, message = "A string de respostas deve ter no máximo 86 caracteres.")
  @Pattern(regexp = "^[0-5]*$", message = "A string de respostas deve conter apenas dígitos entre 0 e 5.")
  private String answers;

}
