package com.fusiontech.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//Qəbul edilən request-dəki token-i filter zəncirindən (filter chain) keçirmək üçün Spring Security-dən
//hazır gələn OncePerRequestFilter class-ındakı "doFilterInternal" metodundan istifadə edirik. Həmin
//metodu proyektə uyğunlaşdırmaq üçün isə override edib konfiqurasiyaları yazırıq.
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;
    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Request-in header-indəki "Authorization" parametrini götürür
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        //Authorization boş deyilsə və "Bearer " ilə başlayırsa deməli token var. Token-i çıxardır.
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtService.extractUsername(token);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
            //Token doğrulandıqdan sonra filterdən keçirdilir
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, SecurityContextHolder.getContext(), userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        }
        //Sonrakı filterə keçilir
        filterChain.doFilter(request, response);
    }
}
