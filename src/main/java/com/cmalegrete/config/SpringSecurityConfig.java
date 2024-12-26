package com.cmalegrete.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableMethodSecurity
@EnableWebSecurity
@Configuration
public class SpringSecurityConfig {

        @Autowired
        private JwtFilter jwtFilter;

        @Autowired
        private UserDetailsService userDetailsService;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                return http.csrf(csrf -> csrf.disable())
                        .authorizeHttpRequests(request -> request
                                        .requestMatchers(HttpMethod.POST, "/associe").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/associe/reenviar-email").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/entrar/**").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/fale-conosco").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/enviar-contrato").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/entrar").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/enviar-contrato").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/fale-conosco").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/eventos").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/institucional").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/associe").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/reservas").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/horarios").permitAll()
                                        .requestMatchers("/resources/**", "/static/**", "/css/**", "/js/**","/images/**","/webjars/**").permitAll()
                                        .anyRequest().authenticated())
                        // .httpBasic(Customizer.withDefaults())
                        .sessionManagement(session -> session
                                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                        .build();
        }

        @Bean
        public AuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
                provider.setPasswordEncoder(new BCryptPasswordEncoder());
                provider.setUserDetailsService(userDetailsService);
                return provider;
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();

        }

}
