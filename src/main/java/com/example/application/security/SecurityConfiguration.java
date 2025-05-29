package com.example.application.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurity {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Desactiva la protección CSRF (no la necesitas si no haces login real)
        http.csrf().disable();

        // Permite el acceso a TODAS las rutas sin autenticación
        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        // No se usa login real, así que no configuramos ninguna vista de login
    }
}
