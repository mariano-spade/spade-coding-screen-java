package com.spade.codingscreen.repository;

import com.spade.codingscreen.model.Corporation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Corporation entities.
 */
@Repository
public interface CorporationRepository extends JpaRepository<Corporation, UUID> {

    Optional<Corporation> findByWebsite(String website);

    Optional<Corporation> findByDoingBusinessAs(String doingBusinessAs);

    Optional<Corporation> findByLegalName(String legalName);
}

