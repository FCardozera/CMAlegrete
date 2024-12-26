package com.cmalegrete.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cmalegrete.service.portal.CustomUserDetailsService;


@EnableMethodSecurity
@EnableWebSecurity
@Configuration
public class SpringSecurityConfig {

        @Autowired
        private UserDetailsService userDetailsService;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                return http.csrf(csrf -> csrf.disable())
                        .formLogin(form -> form
                                .loginPage("/login").permitAll()
                                .defaultSuccessUrl("/dashboard", true))
                        .logout(LogoutConfigurer::permitAll)
                        .authorizeHttpRequests(request -> request
                                .requestMatchers(HttpMethod.POST, "/associe").permitAll()
                                .requestMatchers(HttpMethod.POST, "/associe/reenviar-email").permitAll()
                                .requestMatchers(HttpMethod.POST, "/fale-conosco").permitAll()
                                .requestMatchers(HttpMethod.POST, "/enviar-contrato").permitAll()
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
