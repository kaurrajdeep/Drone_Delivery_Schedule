package com.company.domain;

/**
 * @author rajdeep kaur
 */
public class CustomerReceipt {
    private String identifier;
    private Time arrivalTimestamp;

    /**
     * Default constructor
     * @param identifier the order identifier
     * @param arrivalTimestamp the order arrival timestamp
     */
    public CustomerReceipt(String identifier, Time arrivalTimestamp) {
        this.identifier = identifier;
        this.arrivalTimestamp = arrivalTimestamp;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public String toString() {
        return identifier + " " + arrivalTimestamp.toString();
    }
}
