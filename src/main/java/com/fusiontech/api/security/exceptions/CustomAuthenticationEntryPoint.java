package com.fusiontech.api.security.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fusiontech.api.security.payloads.AuthErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

//Spring Security-də uğursuz autentifikasiya zamanı avtomatik olaraq AuthenticationEntryPoint
//interface-inin commence() metodu çağırılır. Həmin metodu override edib konfiqurasiyasını dəyişirik
@Component("customAuthenticationEntryPoint")
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        //Authentication uğursuz olduqda qaytarılacaq məlumat obyektini tərtib edirik.
        AuthErrorResponse errResponse = new AuthErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Authentication failed");
        //Response obyektinin content tipini JSON olaraq təyin edirik.
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        //HTTP statusunu 401 (Unauthorized) olaraq təyin edirik.
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        //Response obyektinin çıxış axınına məlumat yazmaq üçün OutputStream açırıq.
        OutputStream responseStream = response.getOutputStream();
        //ObjectMapper ilə AuthError obyektini JSON formatına çeviririk və axına yazırıq.
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(responseStream, errResponse);
        //Axına yazdığımız məlumatı göndərmək üçün flush() çağırırıq.
        responseStream.flush();
    }
}
