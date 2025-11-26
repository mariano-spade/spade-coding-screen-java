package com.spade.codingscreen.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;

/**
 * Entity representing a location.
 */
@Entity
@Table(name = "spade_location", indexes = {
    @Index(name = "idx_location_name", columnList = "name"),
    @Index(name = "idx_location_street_address", columnList = "street_address"),
    @Index(name = "idx_location_city", columnList = "city"),
    @Index(name = "idx_location_postal_code", columnList = "postal_code"),
    @Index(name = "idx_location_store_id", columnList = "store_id"),
    @Index(name = "idx_location_h3_cell", columnList = "h3_cell")
})
public class Location {

    @Id
    @Column(name = "id", columnDefinition = "TEXT")
    private UUID id;

    @Column(name = "name", columnDefinition = "TEXT")
    private String name;

    @Column(name = "street_address", columnDefinition = "TEXT")
    private String streetAddress;

    @Column(name = "address_line_1", columnDefinition = "TEXT")
    private String addressLine1;

    @Column(name = "address_line_2", columnDefinition = "TEXT")
    private String addressLine2;

    @Column(name = "city", columnDefinition = "TEXT")
    private String city;

    @Column(name = "state", columnDefinition = "TEXT")
    private String state;

    @Enumerated(EnumType.STRING)
    @Column(name = "country", columnDefinition = "TEXT")
    private Countries country = Countries.USA;

    @Column(name = "postal_code", columnDefinition = "TEXT")
    private String postalCode;

    @Column(name = "store_id", columnDefinition = "TEXT")
    private String storeId;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lon")
    private Double lon;

    @Column(name = "h3_cell", columnDefinition = "TEXT")
    private String h3Cell;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "corporation_id", nullable = false)
    private Corporation corporation;

    public Location() {
    }

    public Location(UUID id, String name, String streetAddress, String addressLine1, String addressLine2,
                    String city, String state, Countries country, String postalCode, String storeId,
                    Double lat, Double lon, String h3Cell, Corporation corporation) {
        this.id = id;
        this.name = name;
        this.streetAddress = streetAddress;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
        this.storeId = storeId;
        this.lat = lat;
        this.lon = lon;
        this.h3Cell = h3Cell;
        this.corporation = corporation;
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

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Countries getCountry() {
        return country;
    }

    public void setCountry(Countries country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getH3Cell() {
        return h3Cell;
    }

    public void setH3Cell(String h3Cell) {
        this.h3Cell = h3Cell;
    }

    public Corporation getCorporation() {
        return corporation;
    }

    public void setCorporation(Corporation corporation) {
        this.corporation = corporation;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}

