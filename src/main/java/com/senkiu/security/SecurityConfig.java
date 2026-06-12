package com.senkiu.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           AuthenticationProvider authenticationProvider) throws Exception {

        http
            .authenticationProvider(authenticationProvider)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/","/login", "/register", "/login-page", "/register-page").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**", "/uploads/**").permitAll()
                .requestMatchers("/programas/**").permitAll()

                .requestMatchers("/productos/**").permitAll()
                .requestMatchers("/rifas/**").permitAll()
                .requestMatchers("/suscripciones").permitAll()
                .requestMatchers("/suscripciones/lista").permitAll()
                .requestMatchers("/sobre-nosotros/**").permitAll()
                .requestMatchers("/api/rifas/**").permitAll()
                .requestMatchers("/tickets/**").hasAnyRole("USER", "EMPRESA", "ONG", "ADMIN")
                .requestMatchers("/api/tickets/**").hasAnyRole("USER", "EMPRESA", "ONG", "ADMIN")
                .requestMatchers("/api/pagos/confirmar/**").permitAll()
                .requestMatchers("/api/pagos/**").authenticated()
                .requestMatchers("/donaciones/**").authenticated()
                .requestMatchers("/carrito/**").permitAll()
                .requestMatchers("/orden/crear/**").permitAll()
                .requestMatchers("/orden/*/pago").permitAll()
                .requestMatchers("/api/notificaciones/**").hasAnyRole("USER", "EMPRESA", "ONG", "ADMIN")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/admin/api/**").hasRole("ADMIN")
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                .requestMatchers("/empresa/solicitud").hasAnyRole("USER", "EMPRESA")
                
                .requestMatchers("/empresa/**").hasRole("EMPRESA")
                .requestMatchers("/api/empresa/**").hasRole("EMPRESA")
                .requestMatchers("/suscripciones/suscripcion-pago").hasAnyRole("USER", "EMPRESA", "ONG")
                .requestMatchers("/orden/**").hasAnyRole("USER", "EMPRESA", "ONG", "ADMIN")
                .requestMatchers("/donaciones/**").hasAnyRole("USER", "EMPRESA", "ONG", "ADMIN")
                .requestMatchers("/entregas/**").hasAnyRole("USER", "EMPRESA", "ADMIN")
                .requestMatchers("/ong/**").hasAnyRole("USER", "ONG")
                .requestMatchers("/api/ong/**").hasRole("ONG")
                .requestMatchers("/api/programas/**").hasRole("ONG")

                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login-page")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/?loginSuccess", true)
                .permitAll()
            )
            .logout(logout -> logout.permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }
}