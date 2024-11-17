package br.com.espacoalcancar.espaco_alcancar_app_api.professional.exceptions;

public class ProfessionalAlreadyExists extends RuntimeException {
  public ProfessionalAlreadyExists(String message) {
    super(message);
  }
}
