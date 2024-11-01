package br.com.espacoalcancar.espaco_alcancar_app_api.user.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.lang.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
  Optional<UserEntity> findByName(String name);

  Optional<UserEntity> findByEmail(String email);

  @EntityGraph(attributePaths = { "children" })
  @NonNull
  Optional<UserEntity> findById(@NonNull Integer id);
}
