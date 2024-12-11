// package com.cmalegrete.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.Customizer;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.builders.WebSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.web.servlet.config.annotation.EnableWebMvc;

// // import com.cmalegrete.jwt.JwtAuthenticationEntryPoint;
// // import com.cmalegrete.jwt.JwtAuthorizationFilter;

// @EnableMethodSecurity
// @EnableWebMvc
// @Configuration
// public class SpringSecurityConfig {

//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//         return http
//                 .csrf(csrf -> csrf.disable())
//                 .formLogin(form -> form.disable())
//                 .httpBasic(basic -> basic.disable())
//                 .cors(Customizer.withDefaults())
//                 .authorizeHttpRequests(auth -> auth
//                         .requestMatchers(HttpMethod.POST, "/entrar/**").permitAll()
//                         .requestMatchers(HttpMethod.GET, "/entrar").permitAll()
//                         .requestMatchers(HttpMethod.GET, "/").permitAll()
//                         .requestMatchers(HttpMethod.GET, "/enviar-contrato").permitAll()
//                         .requestMatchers(HttpMethod.POST, "/enviar-contrato").permitAll()
//                         .requestMatchers(HttpMethod.GET, "/fale-conosco").permitAll()
//                         .requestMatchers(HttpMethod.POST, "/fale-conosco").permitAll()
//                         .requestMatchers(HttpMethod.GET, "/eventos").permitAll()
//                         .requestMatchers(HttpMethod.GET, "/institucional").permitAll()
//                         .requestMatchers(HttpMethod.GET, "/associe").permitAll()
//                         .requestMatchers(HttpMethod.POST, "/associe").permitAll()
//                         .requestMatchers(HttpMethod.GET, "/associe/reenviar-email").permitAll()
//                         .requestMatchers(HttpMethod.GET, "/reservas").permitAll()
//                         .requestMatchers(HttpMethod.GET, "/horarios").permitAll()
//                         .requestMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
//                         .anyRequest().authenticated())
//                 .sessionManagement(
//                         session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                 // .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
//                 // .exceptionHandling(exception -> exception
//                 //         .authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
//                 .build();
//     }

//     // @Bean
//     // public JwtAuthorizationFilter jwtAuthorizationFilter() {
//     //     return new JwtAuthorizationFilter();
//     // }

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     @Bean
//     public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
//             throws Exception {
//         return authenticationConfiguration.getAuthenticationManager();
//     }

// }
