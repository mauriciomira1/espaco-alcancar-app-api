package br.com.espacoalcancar.espaco_alcancar_app_api.user.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
  Optional<UserEntity> findByEmail(String email);

  Optional<UserEntity> findByName(String name);
}
