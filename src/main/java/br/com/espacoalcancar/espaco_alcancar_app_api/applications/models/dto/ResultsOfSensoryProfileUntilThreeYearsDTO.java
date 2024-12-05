package br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.dto;

import lombok.Data;

@Data
public class ResultsOfSensoryProfileUntilThreeYearsDTO {

  private Integer exploracao;
  private Integer esquiva;
  private Integer sensibilidade;
  private Integer observacao;
  private Integer geral;
  private Integer auditivo;
  private Integer visual;
  private Integer tato;
  private Integer movimentos;
  private Integer sensibilidadeOral;
  private Integer comportamental;
}
