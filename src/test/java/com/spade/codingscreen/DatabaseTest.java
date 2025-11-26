package com.spade.codingscreen;

import com.spade.codingscreen.model.Corporation;
import com.spade.codingscreen.model.Countries;
import com.spade.codingscreen.model.Location;
import com.spade.codingscreen.repository.CorporationRepository;
import com.spade.codingscreen.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for database operations.
 * Equivalent to test_database_works in Python.
 */
@SpringBootTest
@Transactional
public class DatabaseTest {

    @Autowired
    private CorporationRepository corporationRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Test
    public void testDatabaseWorks() {
        // Clean up any existing data for this test
        locationRepository.deleteAll();
        corporationRepository.deleteAll();

        // Create corporation
        Corporation corp = new Corporation(
            UUID.randomUUID(),
            "Jim's Conglomerate LLC",
            "Jim's Hardware Supplies",
            "https://jimshardware.test"
        );
        corporationRepository.save(corp);

        // Create location
        Location location = new Location(
            UUID.randomUUID(),
            "Jim's Hardware Store",
            "123 Main St",
            null,
            null,
            "Smallville",
            "NY",
            Countries.USA,
            "12345",
            "1234",
            40.7128,
            -74.0060,
            null,
            corp
        );
        locationRepository.save(location);

        // Verify counts
        assertEquals(1, locationRepository.count());
        assertEquals(1, corporationRepository.count());
    }

    @Test
    public void testCorporationCreation() {
        Corporation corp = new Corporation(
            UUID.randomUUID(),
            "Test Legal Name",
            "Test DBA",
            "https://test.com"
        );
        Corporation saved = corporationRepository.save(corp);

        assertEquals(corp.getId(), saved.getId());
        assertEquals("Test Legal Name", saved.getLegalName());
        assertEquals("Test DBA", saved.getDoingBusinessAs());
        assertEquals("https://test.com", saved.getWebsite());
    }

    @Test
    public void testLocationWithCorporation() {
        Corporation corp = new Corporation(
            UUID.randomUUID(),
            "Parent Corp",
            "Parent DBA",
            "https://parent.com"
        );
        corporationRepository.save(corp);

        Location location = new Location();
        location.setId(UUID.randomUUID());
        location.setName("Test Location");
        location.setStreetAddress("456 Test Ave");
        location.setCity("Test City");
        location.setState("CA");
        location.setCountry(Countries.USA);
        location.setPostalCode("90210");
        location.setLat(34.0901);
        location.setLon(-118.4065);
        location.setCorporation(corp);

        Location saved = locationRepository.save(location);

        assertEquals("Test Location", saved.getName());
        assertEquals("Test City", saved.getCity());
        assertEquals("CA", saved.getState());
        assertEquals(corp.getId(), saved.getCorporation().getId());
    }
}

