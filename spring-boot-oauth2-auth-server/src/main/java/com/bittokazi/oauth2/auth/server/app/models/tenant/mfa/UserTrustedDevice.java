package com.bittokazi.oauth2.auth.server.app.models.tenant.mfa;

import java.io.Serializable;

import com.bittokazi.oauth2.auth.server.app.models.tenant.User;
import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_trusted_device")
@Proxy(lazy = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserTrustedDevice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_trusted_device_generator")
    @SequenceGenerator(name = "user_trusted_device_generator", sequenceName = "user_trusted_device_sequence", initialValue = 1, allocationSize = 1)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @Fetch(value = FetchMode.SELECT)
    @JoinColumn(name = "user_id", nullable = true, insertable = true, updatable = false)
    private User user;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "instance_id")
    private String instanceId;

    @Column(name = "device_ip")
    private String deviceIp;

    @Column(name = "user_agent")
    private String userAgent;

}
