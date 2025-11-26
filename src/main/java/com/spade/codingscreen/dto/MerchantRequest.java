package com.spade.codingscreen.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for incoming merchant matching requests.
 */
public class MerchantRequest {

    @JsonProperty("merchantName")
    private String merchantName;

    @JsonProperty("address")
    private String address;

    @JsonProperty("city")
    private String city;

    @JsonProperty("region")
    private String region;

    @JsonProperty("postalCode")
    private String postalCode;

    public MerchantRequest() {
    }

    public MerchantRequest(String merchantName, String address, String city, String region, String postalCode) {
        this.merchantName = merchantName;
        this.address = address;
        this.city = city;
        this.region = region;
        this.postalCode = postalCode;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public String toString() {
        return "MerchantRequest{" +
                "merchantName='" + merchantName + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", region='" + region + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}

