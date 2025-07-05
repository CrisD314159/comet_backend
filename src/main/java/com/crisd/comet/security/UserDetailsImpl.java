package com.crisd.comet.security;

import com.crisd.comet.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

@Getter
public class UserDetailsImpl implements UserDetails {

    /**
     * Class used to personalize the user that is logged in,
     * with this, we can retrieve the user data (like id or name) from the controllers
     * just when the JWT Access token is successfully verified for spring security
     */

    private final UUID id;
    private final String email;
    private final String password;

    public UserDetailsImpl(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // o Collections.emptyList() si no manejas roles todavía
    }

    @Override public String getUsername() { return email; }
    @Override public String getPassword() { return password; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
