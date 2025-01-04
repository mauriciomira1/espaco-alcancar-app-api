package br.com.espacoalcancar.espaco_alcancar_app_api.user.repositories;

import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.UserRequest;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.entities.UserEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

class UserRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Test
    void findByName() {
    }

    /*private UUID createUser(UserRequest data) {
        newUser = new UserRequest(data);
    }*/
}