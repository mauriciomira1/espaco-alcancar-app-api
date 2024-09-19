package br.com.espacoalcancar.espaco_alcancar_app_api.user.controllers;

import org.springframework.web.bind.annotation.RestController;

import br.com.espacoalcancar.espaco_alcancar_app_api.user.services.ChildService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/user/children")
public class ChildController {

  @Autowired
  ChildService service;

<<<<<<< HEAD
  /*
   * @PostMapping()
   * public Integer create(@RequestBody ChildRequest request) {
   * ChildEntity entity = new ChildEntity();
   * 
   * entity.setBirth(request.getBirth());
   * entity.setName(request.getName());
   * entity.setGender(request.getGender());
   * entity.setUserEntity(null);
   * 
   * service.create(request);
   * }
   */
=======
  // @PostMapping()
  // public Integer create(@RequestBody ChildRequest request) {
  // ChildEntity entity = new ChildEntity();

  // entity.setBirth(request.getBirth());
  // entity.setName(request.getName());
  // entity.setGender(request.getGender());
  // entity.setUserEntity(null);

  // service.create(request);
  // }
>>>>>>> 8a107e8db09a3c9597b011d00130b8199213af63

}
