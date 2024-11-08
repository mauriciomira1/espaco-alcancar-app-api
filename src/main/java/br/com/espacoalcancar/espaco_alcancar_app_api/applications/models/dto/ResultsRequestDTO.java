package br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.dto;

import java.util.List;

import lombok.Data;

@Data
public class ResultsRequestDTO {

  private Integer resultId;
  private List<Integer> answers;

}
