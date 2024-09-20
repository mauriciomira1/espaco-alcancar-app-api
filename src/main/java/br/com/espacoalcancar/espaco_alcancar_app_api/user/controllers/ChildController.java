package br.com.espacoalcancar.espaco_alcancar_app_api.user.controllers;

import org.springframework.web.bind.annotation.RestController;

import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.ChildRequest;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.services.ChildService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/user/children")
public class ChildController {

  @Autowired
  ChildService service;

  @PostMapping("/new")
  public Integer create(@RequestBody ChildRequest childRequest, HttpServletRequest request) {

    return this.service.create(childRequest, request);

  }

}
