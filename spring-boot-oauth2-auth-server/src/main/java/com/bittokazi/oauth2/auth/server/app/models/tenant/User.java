package com.bittokazi.oauth2.auth.server.app.models.tenant;

import com.bittokazi.oauth2.auth.server.app.models.base.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Proxy;
import org.springframework.boot.jackson.JsonMixin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;

/**
 * @author Bitto Kazi
 */

@Entity
@Proxy(lazy = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseModel implements UserDetails, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "first_name", length = 128, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 128, nullable = false)
    private String lastName;

    @Column(name = "email", unique = true, length = 128, nullable = false)
    private String email;

    @Column(name = "user_name", length = 128, unique = true, nullable = false)
    private String username;

    @Column(length = 128, nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column
    private Boolean enabled;

    @Column(name = "change_password")
    private Boolean changePassword;

    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH })
    @Fetch(value = FetchMode.SELECT)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<Role>();

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "dob")
    private Date dob;

    @Column(name = "image_name")
    private String imageName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "image_absolute_path")
    private String imageAbsolutePath;

    @Column(name = "last_login")
    private Date lastLogin;

    @Column(name = "email_verified")
    private Boolean emailVerified;

    private String address;

    private String gender;

    @Column(name = "two_fa_enabled")
    private Boolean twoFaEnabled;

    @Transient
    private String newPassword;

    @Transient
    private String newConfirmPassword;

    @Transient
    private List<GrantedAuthority> authorities;

    @Transient
    private String avatarImage;

    @Transient
    private boolean adminTenantUser = false;

    @Transient
    private String currentPassword;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> rls = new ArrayList<GrantedAuthority>();
        for (Role role : roles) {
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
        return enabled;
    }

}

