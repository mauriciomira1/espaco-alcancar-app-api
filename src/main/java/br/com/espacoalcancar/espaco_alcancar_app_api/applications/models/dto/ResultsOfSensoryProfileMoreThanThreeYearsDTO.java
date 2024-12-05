package br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.dto;

import lombok.Data;

@Data
public class ResultsOfSensoryProfileMoreThanThreeYearsDTO {

  private Integer exploracao;
  private Integer esquiva;
  private Integer sensibilidade;
  private Integer observacao;
  private Integer auditivo;
  private Integer visual;
  private Integer tato;
  private Integer movimentos;
  private Integer posicaoDoCorpo;
  private Integer oral;
  private Integer conduta;
  private Integer socioemocional;
  private Integer atencao;

}
