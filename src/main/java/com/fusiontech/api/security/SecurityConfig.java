package com.fusiontech.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    @Qualifier("customAuthenticationEntryPoint")
    AuthenticationEntryPoint authEntryPoint;

    //Hərkəsə açıq olan url-lərin siyahısı
    private static final String[] SWAGGER_WHITELIST = {
            "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/swagger-resources"};

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //csrf qorumasını deaktiv et
        http.csrf(c -> c.disable())
                .authorizeHttpRequests(request -> {
                    //SWAGGER_WHITELIST siyahısındakı url-ləri hamıya icazə ver
                    request.requestMatchers(SWAGGER_WHITELIST).permitAll();
                    //register və login url-lərini hamıya icazə ver
                    request.requestMatchers("api/auth/register", "api/auth/login", "api/auth/refresh-token").permitAll();
                    //"/api/category" ilə başlayan url-lərdə login tələb et
                    request.requestMatchers("/api/category/**").authenticated();
                    //Digər url-lərdə login tələb et
                    request.anyRequest().authenticated();
                });

        http.httpBasic(basic -> basic.authenticationEntryPoint(authEntryPoint))
                .exceptionHandling(Customizer.withDefaults());
        //request-i qəbul etməzdən əvvəl filter tətbiq et
        http.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public JwtAuthFilter authenticationTokenFilter() throws Exception {
        return new JwtAuthFilter();
    }
}
