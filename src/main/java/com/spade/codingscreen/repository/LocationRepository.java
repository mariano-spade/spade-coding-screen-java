package com.spade.codingscreen.repository;

import com.spade.codingscreen.model.Corporation;
import com.spade.codingscreen.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Location entities.
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {

    Optional<Location> findByStoreIdAndCorporation(String storeId, Corporation corporation);

    Optional<Location> findByNameAndCityAndStateAndCorporation(String name, String city, String state, Corporation corporation);
}

