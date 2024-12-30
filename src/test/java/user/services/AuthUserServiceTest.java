import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.AuthUserRequest;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.dto.AuthUserResponse;
import br.com.espacoalcancar.espaco_alcancar_app_api.user.models.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.naming.AuthenticationException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthUserServiceTest {

    @InjectMocks
    AuthUserService authUserService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserService userService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JWTProvider jwt;

    @Test
    public void testExecute_Success() throws AuthenticationException {
        AuthUserRequest request = new AuthUserRequest("test@test.com", "12345678");

        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setEmail("test@test.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(userService.getUserRoles(anyString())).thenReturn(List.of("ROLE_USER"));
        when(jwt.generateToken(any(UUID.class), any(Instant.class))).thenReturn("token");

        AuthUserResponse response = authUserService.execute(request);

        assertNotNull(response);
        assertEquals("token", response.getToken());
        assertEquals("token", response.getRefreshToken());
        assertEquals(List.of("ROLE_USER"), response.getRoles());
    }

    @Test
    public void testExecute_UserNotFound() {
        AuthUserRequest request = new AuthUserRequest("test@example.com", "password");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        request.setEmail("test@example.com");
        request.setPassword("password");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> authUserService.execute(request));
    }

    @Test
    public void testExecute_InvalidPassword() {
        AuthUserRequest request = new AuthUserRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(AuthenticationException.class, () -> authUserService.execute(request));
    }

    @Test
    public void testRefreshToken_Success() throws AuthenticationException {
        String refreshToken = "validRefreshToken";
        UUID userId = UUID.randomUUID();

        when(jwt.validateToken(anyString())).thenReturn(userId.toString());
        when(jwt.generateToken(any(UUID.class), any(Instant.class))).thenReturn("newToken");

        String newToken = authUserService.refreshToken(refreshToken);

        assertEquals("newToken", newToken);
    }

    @Test
    public void testRefreshToken_InvalidToken() {
        String refreshToken = "invalidRefreshToken";

        when(jwt.validateToken(anyString())).thenReturn(null);

        assertThrows(AuthenticationException.class, () -> authUserService.refreshToken(refreshToken));
    }
}