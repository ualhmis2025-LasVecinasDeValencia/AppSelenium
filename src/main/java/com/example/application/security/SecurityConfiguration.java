package com.example.application.security;

import com.example.application.views.login.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurity {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authorize -> authorize
            .requestMatchers(new AntPathRequestMatcher("/images/*.png")).permitAll()
            .requestMatchers(new AntPathRequestMatcher("/line-awesome/**/*.svg")).permitAll()
            .requestMatchers("/registro", "/login").permitAll()
            .anyRequest().authenticated()
        );

        super.configure(http);
        setLoginView(http, LoginView.class);

        // Redirigir después de login exitoso a la raíz "/"
        http.formLogin().defaultSuccessUrl("/", true);
    }
}
