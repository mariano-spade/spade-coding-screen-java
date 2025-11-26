package com.spade.codingscreen.model;

/**
 * Enum representing supported countries.
 */
public enum Countries {
    USA("USA"),
    CAN("Canada");

    private final String displayName;

    Countries(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

