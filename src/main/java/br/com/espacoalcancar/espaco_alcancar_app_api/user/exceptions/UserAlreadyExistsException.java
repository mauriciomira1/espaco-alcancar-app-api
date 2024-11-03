package br.com.espacoalcancar.espaco_alcancar_app_api.user.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

  public UserAlreadyExistsException(String message) {
    super(message);
  }

}
