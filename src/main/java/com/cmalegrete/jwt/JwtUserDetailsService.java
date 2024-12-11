// package com.cmalegrete.jwt;

// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Service;

// import com.cmalegrete.model.user.UserEntity;
// import com.cmalegrete.model.user.UserRepository;
// import com.cmalegrete.model.user.UserRoleEnum;

// import lombok.RequiredArgsConstructor;

// @RequiredArgsConstructor
// @Service
// public class JwtUserDetailsService implements UserDetailsService {

//     private final UserRepository userRepository;

//     @Override
//     public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//         UserEntity usuario = userRepository.findByEmail(username);
//         return new JwtUserDetails(usuario);
//     }

//     public JwtToken getTokenAuthenticated(String username) {
//         UserRoleEnum role = userRepository.findRoleByEmail(username);
//         return JwtUtils.createToken(username, role.name().substring("ROLE_".length()));
//     }

// }
