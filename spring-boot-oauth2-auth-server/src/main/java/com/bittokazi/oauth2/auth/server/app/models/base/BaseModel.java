package com.bittokazi.oauth2.auth.server.app.models.base;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author bitto kazi
 */

@MappedSuperclass
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
public class BaseModel {

    @Column(name="created_date", insertable = true, updatable = false)
    public Date createdDate;

    @Column(name="updated_date", insertable = true, updatable = true)
    public Date updatedDate;

    @Column(name="created_by")
    public String createdBy;

    @Column(name="updated_by")
    public String updatedBy;

    @Column(name="deleted_at")
    private Date deletedAt;

    @PrePersist
    public void createdAt() {
        this.createdDate = this.updatedDate = new Date();
    }

    @PreUpdate
    public void updatedAt() {
        this.updatedDate = new Date();
    }
}

