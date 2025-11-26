package com.spade.codingscreen.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

/**
 * DTO for solution response containing matched location and corporation.
 */
public class SolutionResponse {

    @JsonProperty("location")
    private LocationInfo location;

    @JsonProperty("corporation")
    private CorporationInfo corporation;

    public SolutionResponse() {
    }

    public SolutionResponse(LocationInfo location, CorporationInfo corporation) {
        this.location = location;
        this.corporation = corporation;
    }

    public LocationInfo getLocation() {
        return location;
    }

    public void setLocation(LocationInfo location) {
        this.location = location;
    }

    public CorporationInfo getCorporation() {
        return corporation;
    }

    public void setCorporation(CorporationInfo corporation) {
        this.corporation = corporation;
    }

    /**
     * Nested class for location information in the response.
     */
    public static class LocationInfo {
        @JsonProperty("id")
        private UUID id;

        @JsonProperty("name")
        private String name;

        public LocationInfo() {
        }

        public LocationInfo(UUID id, String name) {
            this.id = id;
            this.name = name;
        }

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * Nested class for corporation information in the response.
     */
    public static class CorporationInfo {
        @JsonProperty("id")
        private UUID id;

        @JsonProperty("legal_name")
        private String legalName;

        @JsonProperty("doing_business_as")
        private String doingBusinessAs;

        public CorporationInfo() {
        }

        public CorporationInfo(UUID id, String legalName, String doingBusinessAs) {
            this.id = id;
            this.legalName = legalName;
            this.doingBusinessAs = doingBusinessAs;
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
    }
}

