package com.bittokazi.oauth2.auth.server.app.models.tenant;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Proxy;

/**
 * @author Bitto Kazi
 */

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Proxy(lazy = false)
public class Role implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(unique = true, nullable = false, length = 128)
    private String name;

    @Column(nullable = false, length = 128)
    private String title;

    @Column(length = 64)
    private String description;

}
