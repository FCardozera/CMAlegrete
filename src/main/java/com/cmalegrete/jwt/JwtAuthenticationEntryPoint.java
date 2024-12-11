// package com.cmalegrete.jwt;

// import java.io.IOException;

// import org.springframework.security.core.AuthenticationException;
// import org.springframework.security.web.AuthenticationEntryPoint;

// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import lombok.extern.slf4j.Slf4j;

// @Slf4j
// public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

//     @Override
//     public void commence(HttpServletRequest request, HttpServletResponse response,
//             AuthenticationException authException) throws IOException, ServletException {
//         log.info("Http status 401 {}", authException.getMessage());
//         response.setHeader("www-authenticate", "Bearer realm='/v1/auth/login'");
//         response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
//     }

// }
