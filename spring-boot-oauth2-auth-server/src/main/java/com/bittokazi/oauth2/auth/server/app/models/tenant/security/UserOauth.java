package com.bittokazi.oauth2.auth.server.app.models.tenant.security;

import com.bittokazi.oauth2.auth.server.app.models.tenant.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserOauth implements UserDetails, Serializable {

    private String id;

    private String email;

    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private boolean enabled;

    private Set<RoleOauth> roles = new HashSet<RoleOauth>();

    private Collection<? extends GrantedAuthority> authorities;

    private Boolean accountNonLocked = true;

    private Boolean accountNonExpired = true;

    private Boolean credentialsNonExpired = true;


    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(Objects.nonNull(authorities)) return authorities;
        List<GrantedAuthority> rls = new ArrayList<GrantedAuthority>();
        for (RoleOauth role : roles) {
            rls.add(new SimpleGrantedAuthority(role.getName()));
        }
        return rls;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }

}
