package br.com.espacoalcancar.espaco_alcancar_app_api.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class FirebaseTokenFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        // Só tenta validar como Firebase se o token for claramente do Firebase
        // (exemplo: começa com 'eyJhbGciOiJSUzI1NiIsImtpZCI6')
        // Caso contrário, apenas passa para o próximo filtro (JWT)
        if (!token.startsWith("eyJhbGciOiJSUzI1NiIsImtpZCI6")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            String uid = decodedToken.getUid();

            // Cria uma autenticação fake apenas com o UID
            Authentication auth = new UsernamePasswordAuthenticationToken(uid, null, List.of());
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (FirebaseAuthException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
