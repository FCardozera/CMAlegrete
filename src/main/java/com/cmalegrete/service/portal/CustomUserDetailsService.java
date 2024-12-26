package com.cmalegrete.service.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cmalegrete.exception.generic.UserNotExistsException;
import com.cmalegrete.model.user.UserEntity;
import com.cmalegrete.model.user.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String registrationId) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByRegistrationId(registrationId);
        if (user == null) {
            throw new UserNotExistsException("Usuário de matrícula " + registrationId + " não foi encontrado");
        }

        return User.builder()
        .username(user.getRegistrationId())
        .password(user.getPassword())
        .roles(user.getRole().name())
        .build(); 
    }
}
