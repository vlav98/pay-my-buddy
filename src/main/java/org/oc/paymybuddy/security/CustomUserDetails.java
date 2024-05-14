package org.oc.paymybuddy.security;

import lombok.Getter;
import org.oc.paymybuddy.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class CustomUserDetails implements UserDetails {

    private User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }


    /**
     * @return null
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    /**
     * @return hashed password
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * @return string email
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * @return boolean
     */
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    /**
     * @return boolean
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * @return boolean
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * @return boolean
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getName() {
        return user.getFirstName();
    }

}
