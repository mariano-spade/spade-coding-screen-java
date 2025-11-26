package com.spade.codingscreen.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.util.UUID;

/**
 * Entity representing a corporation.
 */
@Entity
@Table(name = "spade_corporation", indexes = {
    @Index(name = "idx_corporation_legal_name", columnList = "legal_name"),
    @Index(name = "idx_corporation_doing_business_as", columnList = "doing_business_as"),
    @Index(name = "idx_corporation_website", columnList = "website")
})
public class Corporation {

    @Id
    @Column(name = "id", columnDefinition = "TEXT")
    private UUID id;

    @Column(name = "legal_name", columnDefinition = "TEXT")
    private String legalName;

    @Column(name = "doing_business_as", columnDefinition = "TEXT")
    private String doingBusinessAs;

    @Column(name = "website", columnDefinition = "TEXT")
    private String website;

    public Corporation() {
    }

    public Corporation(UUID id, String legalName, String doingBusinessAs, String website) {
        this.id = id;
        this.legalName = legalName;
        this.doingBusinessAs = doingBusinessAs;
        this.website = website;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getDoingBusinessAs() {
        return doingBusinessAs;
    }

    public void setDoingBusinessAs(String doingBusinessAs) {
        this.doingBusinessAs = doingBusinessAs;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String toString() {
        return "Corporation{" +
                "id=" + id +
                ", legalName='" + legalName + '\'' +
                ", doingBusinessAs='" + doingBusinessAs + '\'' +
                ", website='" + website + '\'' +
                '}';
    }
}

