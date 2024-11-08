package br.com.espacoalcancar.espaco_alcancar_app_api.applications.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.espacoalcancar.espaco_alcancar_app_api.applications.repositories.ResultsOfSensoryProfileRepository;

public class ResultsOfSensoryProfileService {

  @Autowired
  private ResultsOfSensoryProfileRepository resultsRepository;

  public Integer create(Integer lengthOfAnswersArray) {
    List<Integer> results = new ArrayList<>(lengthOfAnswersArray);

  }
}
