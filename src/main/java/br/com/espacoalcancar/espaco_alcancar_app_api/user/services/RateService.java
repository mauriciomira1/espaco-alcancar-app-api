package br.com.espacoalcancar.espaco_alcancar_app_api.user.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.RateRequest;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.RateResponse;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.entities.RateEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.repositories.RateRepository;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class RateService {

  @Autowired
  RateRepository rateRepository;

  @Autowired
  UserRepository userRepository;

  // Adicionar uma avaliação
  public Integer create(RateRequest request, HttpServletRequest httpServletRequest) {

    // Recuperando o ID do usuário configurado no SecurityFilter.java
    var userIdObject = httpServletRequest.getAttribute("user_id");
    Integer userId = Integer.valueOf(userIdObject.toString());

    RateEntity rateEntity = new RateEntity();

    rateEntity.setStars(request.getStars());
    rateEntity.setComment(request.getComment());
    rateEntity.setUserId(userId);
    rateEntity.setCreatedAt(LocalDateTime.now());

    return this.rateRepository.save(rateEntity).getId();

  }

  // Buscar todas as avaliações de um usuário baseado no seu ID
  public Iterable<RateResponse> findByUserId(Integer userId) {
    List<RateEntity> rateEntities = this.rateRepository.findAllByUserId(userId);
    return rateEntities.stream().map(this::convertToRateResponse).collect(Collectors.toList());
  }

  private RateResponse convertToRateResponse(RateEntity entity) {
    RateResponse response = new RateResponse();
    BeanUtils.copyProperties(entity, response);
    return response;
  }
}
