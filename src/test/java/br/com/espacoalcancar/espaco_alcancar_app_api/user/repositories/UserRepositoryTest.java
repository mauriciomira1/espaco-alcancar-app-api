package br.com.espacoalcancar.espaco_alcancar_app_api.user.repositories;

import br.com.espacoalcancar.espaco_alcancar_app_api.security.PasswordEncodingConfig;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.Address;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.ProfileType;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.Relationship;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.UserRequest;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.entities.UserEntity;
import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.UUID;

@DataJpaTest
@Import(PasswordEncodingConfig.class)
class UserRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("Should get User successfuly from DB")
    void findByNameSuccess() {
        String name = "User Test";
        UserRequest data = new UserRequest(name, "12345678", "123456789", "test@test.com", Relationship.OTHER,
                new Address(), new ProfileType(true, false, false));
        this.createUser(data);

        Optional<UserEntity> result = this.userRepository.findByName(name);
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should not get User successfuly from DB when name is not found")
    void findByNameNotFound() {
        String name = "User Test";

        Optional<UserEntity> result = this.userRepository.findByName(name);
        assertThat(result.isEmpty()).isTrue();
    }

    private UUID createUser(UserRequest data) {
        UserEntity newUser = new UserEntity();

        data.setProfileType(new ProfileType(true, false, false));
        newUser.setName(data.getName());
        newUser.setAddress(data.getAddress());
        newUser.setEmail(data.getEmail());
        newUser.setRelationship(data.getRelationship());
        newUser.setPhone(data.getPhone());
        newUser.setProfileType(data.getProfileType());
        newUser.setPassword(this.passwordEncoder.encode(data.getPassword()));
        this.entityManager.persist(newUser);

        return entityManager.merge(newUser).getId();
    }

}