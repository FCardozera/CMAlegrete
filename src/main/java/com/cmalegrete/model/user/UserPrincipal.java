package com.cmalegrete.model.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {

    private UserEntity user;

    public UserPrincipal(UserEntity user) {
        this.user = user;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user.getRole() == UserRoleEnum.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ADMIN"), new SimpleGrantedAuthority("ASSISTANT"),
                    new SimpleGrantedAuthority("MEMBER"));
        }

        if (user.getRole() == UserRoleEnum.ASSISTANT) {
            return List.of(new SimpleGrantedAuthority("ASSISTANT"), new SimpleGrantedAuthority("MEMBER"));
        }

        return List.of(new SimpleGrantedAuthority("MEMBER"));
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return user.getRegistrationId();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
