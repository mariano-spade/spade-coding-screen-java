package com.spade.codingscreen.cli;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.spade.codingscreen.model.Corporation;
import com.spade.codingscreen.model.Countries;
import com.spade.codingscreen.model.Location;
import com.spade.codingscreen.repository.CorporationRepository;
import com.spade.codingscreen.repository.LocationRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * CLI command to load corporations and locations from CSV files.
 * Equivalent to Django's load_csv management command.
 */
@Component
public class LoadCsvCommand {

    private final CorporationRepository corporationRepository;
    private final LocationRepository locationRepository;

    private Map<UUID, Corporation> externalIdToCorp = new HashMap<>();
    private int createdCorps = 0;
    private int updatedCorps = 0;
    private int createdLocs = 0;
    private int updatedLocs = 0;

    public LoadCsvCommand(CorporationRepository corporationRepository, LocationRepository locationRepository) {
        this.corporationRepository = corporationRepository;
        this.locationRepository = locationRepository;
    }

    public void run(String[] args) {
        // Reset state from any previous runs (singleton can be invoked multiple times)
        externalIdToCorp.clear();
        createdCorps = 0;
        updatedCorps = 0;
        createdLocs = 0;
        updatedLocs = 0;

        String corporationsPath = "data/corporations.csv";
        String locationsPath = "data/locations.csv";
        boolean createMissingCorps = false;
        boolean dryRun = false;

        // Parse arguments
        for (int i = 0; i < args.length; i++) {
            if ("--corporations".equals(args[i]) && i + 1 < args.length) {
                corporationsPath = args[++i];
            } else if ("--locations".equals(args[i]) && i + 1 < args.length) {
                locationsPath = args[++i];
            } else if ("--create-missing-corps".equals(args[i])) {
                createMissingCorps = true;
            } else if ("--dry-run".equals(args[i])) {
                dryRun = true;
            }
        }

        Path corpPath = Paths.get(corporationsPath);
        Path locPath = Paths.get(locationsPath);

        if (!Files.exists(corpPath)) {
            System.out.println("WARNING: Corporations CSV not found: " + corporationsPath + " (continuing; will only load locations)");
        }

        if (!Files.exists(locPath)) {
            System.err.println("ERROR: Locations CSV not found: " + locationsPath);
            return;
        }

        try {
            if (Files.exists(corpPath)) {
                loadCorporations(corpPath, dryRun);
            }
            loadLocations(locPath, createMissingCorps, dryRun);

            String summary = String.format(
                "Done. Corporations: +%d created, ~%d updated. Locations: +%d created, ~%d updated.",
                createdCorps, updatedCorps, createdLocs, updatedLocs
            );
            System.out.println(summary);
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Transactional
    public void loadCorporations(Path csvPath, boolean dryRun) throws IOException, CsvValidationException {
        try (CSVReader reader = new CSVReader(new FileReader(csvPath.toFile()))) {
            String[] headers = reader.readNext();
            if (headers == null) return;

            List<String> headerList = Arrays.asList(headers);

            // Validate required headers
            int corpIdIdx = headerList.indexOf("corporation_id");
            if (corpIdIdx < 0) {
                System.err.println("ERROR: corporations CSV missing required column: corporation_id");
                return;
            }

            int legalNameIdx = headerList.indexOf("legal_name");
            int dbaIdx = headerList.indexOf("doing_business_as");
            int websiteIdx = headerList.indexOf("website");

            String[] row;
            while ((row = reader.readNext()) != null) {
                String corpIdStr = getOrNull(row, corpIdIdx);
                if (corpIdStr == null) {
                    System.out.println("WARNING: Skipping corporation row with missing ID");
                    continue;
                }
                UUID extId = UUID.fromString(corpIdStr);
                String legalName = getOrNull(row, legalNameIdx);
                String dba = getOrNull(row, dbaIdx);
                String website = getOrNull(row, websiteIdx);

                // Match priority: website, dba, legal_name
                Corporation corp = null;
                if (website != null) {
                    corp = corporationRepository.findByWebsite(website).orElse(null);
                }
                if (corp == null && dba != null) {
                    corp = corporationRepository.findByDoingBusinessAs(dba).orElse(null);
                }
                if (corp == null && legalName != null) {
                    corp = corporationRepository.findByLegalName(legalName).orElse(null);
                }

                if (corp == null) {
                    corp = new Corporation(extId, legalName, dba, website);
                    if (!dryRun) {
                        corporationRepository.save(corp);
                    }
                    createdCorps++;
                }

                // Track mapping from external id to the resolved corp
                externalIdToCorp.put(extId, corp);
            }
        }
    }

    @Transactional
    public void loadLocations(Path csvPath, boolean createMissingCorps, boolean dryRun) throws IOException, CsvValidationException {
        try (CSVReader reader = new CSVReader(new FileReader(csvPath.toFile()))) {
            String[] headers = reader.readNext();
            if (headers == null) return;

            List<String> headerList = Arrays.asList(headers);

            // Check required columns
            if (!headerList.contains("name") || !headerList.contains("state") || !headerList.contains("country")) {
                System.err.println("ERROR: locations CSV missing required columns");
                return;
            }

            boolean hasCity = headerList.contains("city") || headerList.contains("city1");
            if (!hasCity) {
                System.err.println("ERROR: locations CSV must include either 'city' or 'city1' column");
                return;
            }

            String[] row;
            while ((row = reader.readNext()) != null) {
                String corpIdStr = getValueByHeader(row, headerList, "corporation_id");
                UUID corpId = (corpIdStr != null) ? UUID.fromString(corpIdStr) : null;
                String dba = getValueByHeader(row, headerList, "doing_business_as");
                String website = getValueByHeader(row, headerList, "website");
                String locName = getValueByHeader(row, headerList, "name");

                // Resolve corporation
                Corporation corp = null;
                if (corpId != null) {
                    corp = externalIdToCorp.get(corpId);
                }
                if (corp == null && website != null) {
                    corp = corporationRepository.findByWebsite(website).orElse(null);
                }
                if (corp == null && dba != null) {
                    corp = corporationRepository.findByDoingBusinessAs(dba).orElse(null);
                }
                if (corp == null && locName != null) {
                    corp = corporationRepository.findByDoingBusinessAs(locName).orElse(null);
                }
                if (corp == null && locName != null) {
                    corp = corporationRepository.findByLegalName(locName).orElse(null);
                }

                if (corp == null && createMissingCorps) {
                    corp = new Corporation(corpId, null, dba != null ? dba : locName, website);
                    if (!dryRun) {
                        corporationRepository.save(corp);
                    }
                }

                if (corp == null) {
                    System.out.println("WARNING: Skipping location without resolvable corporation: " +
                        getValueByHeader(row, headerList, "id") + " (" + locName + ")");
                    continue;
                }

                // Build location
                String locIdStr = getValueByHeader(row, headerList, "id");
                if (locIdStr == null) {
                    System.out.println("WARNING: Skipping location row with missing ID");
                    continue;
                }
                UUID locId = UUID.fromString(locIdStr);
                String streetAddress = getValueByHeader(row, headerList, "street_address");
                String addressLine1 = getValueByHeader(row, headerList, "address_line_1");
                String addressLine2 = getValueByHeader(row, headerList, "address_line_2");
                String city = getValueByHeader(row, headerList, "city");
                if (city == null) {
                    city = getValueByHeader(row, headerList, "city1");
                }
                String state = getValueByHeader(row, headerList, "state");
                String countryStr = getValueByHeader(row, headerList, "country");
                Countries country = parseCountry(countryStr);
                String postalCode = getValueByHeader(row, headerList, "postal_code");
                String storeId = getValueByHeader(row, headerList, "store_id");
                Double lat = parseDouble(getValueByHeader(row, headerList, "lat"));
                Double lon = parseDouble(getValueByHeader(row, headerList, "lon"));
                String h3Cell = getValueByHeader(row, headerList, "h3_cell");

                // Check for existing location
                Location existing = null;
                if (storeId != null && !storeId.isEmpty()) {
                    existing = locationRepository.findByStoreIdAndCorporation(storeId, corp).orElse(null);
                }
                if (existing == null) {
                    existing = locationRepository.findByNameAndCityAndStateAndCorporation(locName, city, state, corp).orElse(null);
                }

                if (existing == null) {
                    Location location = new Location(locId, locName, streetAddress, addressLine1, addressLine2,
                        city, state, country, postalCode, storeId, lat, lon, h3Cell, corp);
                    if (!dryRun) {
                        locationRepository.save(location);
                    }
                    createdLocs++;
                } else {
                    // Update existing
                    boolean changed = false;
                    if (!equals(existing.getName(), locName)) { existing.setName(locName); changed = true; }
                    if (!equals(existing.getStreetAddress(), streetAddress)) { existing.setStreetAddress(streetAddress); changed = true; }
                    if (!equals(existing.getAddressLine1(), addressLine1)) { existing.setAddressLine1(addressLine1); changed = true; }
                    if (!equals(existing.getAddressLine2(), addressLine2)) { existing.setAddressLine2(addressLine2); changed = true; }
                    if (!equals(existing.getCity(), city)) { existing.setCity(city); changed = true; }
                    if (!equals(existing.getState(), state)) { existing.setState(state); changed = true; }
                    if (existing.getCountry() != country) { existing.setCountry(country); changed = true; }
                    if (!equals(existing.getPostalCode(), postalCode)) { existing.setPostalCode(postalCode); changed = true; }
                    if (!equals(existing.getStoreId(), storeId)) { existing.setStoreId(storeId); changed = true; }
                    if (!equals(existing.getLat(), lat)) { existing.setLat(lat); changed = true; }
                    if (!equals(existing.getLon(), lon)) { existing.setLon(lon); changed = true; }
                    if (!equals(existing.getH3Cell(), h3Cell)) { existing.setH3Cell(h3Cell); changed = true; }

                    if (changed && !dryRun) {
                        locationRepository.save(existing);
                        updatedLocs++;
                    } else if (changed) {
                        updatedLocs++;
                    }
                }
            }
        }
    }

    private String getOrNull(String[] row, int idx) {
        if (idx < 0 || idx >= row.length) return null;
        String val = row[idx].trim();
        return val.isEmpty() ? null : val;
    }

    private String getValueByHeader(String[] row, List<String> headers, String headerName) {
        int idx = headers.indexOf(headerName);
        return getOrNull(row, idx);
    }

    private Double parseDouble(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Countries parseCountry(String value) {
        if (value == null) return Countries.USA;
        if ("CAN".equalsIgnoreCase(value) || "Canada".equalsIgnoreCase(value)) {
            return Countries.CAN;
        }
        return Countries.USA;
    }

    private boolean equals(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }
}

