package com.bittokazi.oauth2.auth.server.app.models.master;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Proxy;

import java.io.Serializable;

@Entity
@Table(name = "tenant")
@Proxy(lazy = false)
@Setter
@Getter
public class Tenant implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "company_key", length = 255, unique = true, nullable = false)
    private String companyKey;

    @Column
    private boolean enabled;

    @Column(unique = true, nullable = false, length = 255)
    private String name;

    @Column
    private String domain;

    @Column
    private String logo;

    @Column(name = "logo_absolute_path")
    private String logoAbsolutePath;

    @Column(name = "signin_btn_color")
    private String signInBtnColor;

    @Column(name = "reset_password_link")
    private String resetPasswordLink;

    @Column(name = "create_account_link")
    private String createAccountLink;

    @Column(name = "default_redirect_url")
    private String defaultRedirectUrl;

    @Column(name = "enable_config_panel")
    private Boolean enableConfigPanel;
}
