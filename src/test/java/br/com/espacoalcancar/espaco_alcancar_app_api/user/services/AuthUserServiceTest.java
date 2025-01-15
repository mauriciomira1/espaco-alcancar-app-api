package br.com.espacoalcancar.espaco_alcancar_app_api.user.services;

import br.com.espacoalcancar.espaco_alcancar_app_api.providers.JWTProvider;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.AuthUserRequest;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.AuthUserResponse;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.entities.UserEntity;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.repositories.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.naming.AuthenticationException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test AuthUserService Class")
public class AuthUserServiceTest {

    @InjectMocks
    AuthUserService authUserService;

    @Mock
    UserService userService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JWTProvider jwt;

    @Mock
    UserRepository userRepository;

    private AuthUserRequest request;
    private UserEntity user;

    @BeforeEach
    void beforeEachMethod() {
        MockitoAnnotations.openMocks(this);
        request = new AuthUserRequest("test@test.com", "password");
        user = createUserEntity("test@test.com", "encodedPassword");
    }

    @Test
    void testLogin_When_InsertAValidUsernameAndPassword_ShouldReturnSuccess() throws AuthenticationException {

        // When
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwt.generateToken(any(UUID.class), any(Instant.class))).thenReturn("token");
        when(userService.getUserRoles(anyString())).thenReturn(List.of("ROLE_USER"));

        AuthUserResponse response = authUserService.execute(request);

        // Then
        assertNotNull(response);
        assertEquals("token", response.getToken());
        assertEquals("token", response.getRefreshToken());
        assertEquals(List.of("ROLE_USER"), response.getRoles());

        // Capturar os argumentos passados para generateToken
        ArgumentCaptor<UUID> uuidCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<Instant> instantCaptor = ArgumentCaptor.forClass(Instant.class);
        verify(jwt).generateToken(uuidCaptor.capture(), instantCaptor.capture());

        // Verificar se os argumentos capturados são os esperados
        assertEquals(user.getId(), uuidCaptor.getValue());
        assertNotNull(instantCaptor.getValue());

    }

    @Test
    void testLogin_When_PasswordIsIncorrect_ShouldReturnInvalidPassword() {

        // When
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(AuthenticationException.class, () -> authUserService.execute(request));
    }

    @Test
    void testLogin_When_UserNotFound_ShouldReturnAException() {
        AuthUserRequest request = new AuthUserRequest("test@test.com", "password");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(AuthenticationException.class, () -> authUserService.execute(request));
    }

    @Test
    void testRefreshToken_When_RefreshTokenIsValid_ShouldRenewToken() throws AuthenticationException {
        String refreshToken = "refreshToken";
        UUID userId = UUID.randomUUID();

        when(jwt.validateToken(anyString())).thenReturn(userId.toString());
        when(jwt.generateToken(any(UUID.class), any(Instant.class))).thenReturn("newToken");

        String newToken = authUserService.refreshToken(refreshToken);

        assertEquals("newToken", newToken);
    }

    @Test
    void testRefreshToken_When_RefreshTokenIsInvalid_ShouldReturnAException() throws AuthenticationException {
        String refreshToken = "invalidRefreshToken";

        when(jwt.validateToken(anyString())).thenReturn(null);

        assertThrows(AuthenticationException.class, () -> authUserService.refreshToken(refreshToken));
    }

    private UserEntity createUserEntity(String email, String password) {
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }
}