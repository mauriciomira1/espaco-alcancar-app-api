package br.com.espacoalcancar.espaco_alcancar_app_api.applications.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.espacoalcancar.espaco_alcancar_app_api.applications.models.dto.SensoryProfileRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/dashboard/fillout")
public class SensoryProfileController {

  @PostMapping("/sensory-profile")
  public Integer create(@RequestBody SensoryProfileRequest request) {

    return null;
  }

}
