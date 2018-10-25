package com.company.domain;

/**
 * @author rajdeep kaur
 */
public class CustomerOrder implements Comparable<CustomerOrder>{
    private String identifier;
    private CustomerLocation customerLocation;
    private Time orderPlacedTime;
    private Time expectedDeliveryTime;

    public CustomerOrder(String identifier, CustomerLocation customerLocation, Time orderPlacedTime) {
        this.identifier = identifier;
        this.customerLocation = customerLocation;
        this.orderPlacedTime = orderPlacedTime;
    }

    public Time getOrderPlacedTime() {
        return orderPlacedTime;
    }

    public String getIdentifier() {
        return identifier;
    }

    public CustomerLocation getCustomerLocation() {
        return customerLocation;
    }

    /**
     * Gets the total delay time from the time order is placed to delivered
     * @return wait time in seconds
     */
    public int expectedWaitTime() {
        return this.expectedDeliveryTime.getSeconds() - this.orderPlacedTime.getSeconds();
    }

    @Override
    public String toString() {
        return identifier + " " + customerLocation.toString() + " " + orderPlacedTime.toString();
    }

    public void setExpectedDeliveryTime(Time expectedDeliveryTime) {
        this.expectedDeliveryTime = expectedDeliveryTime;
    }

    /**
     * Custom compareTo method
     * Compare two order's expected timestamp, if expected timestamp is null,
     * 	compare two order's distance. If distance is equal,
     * 	then compare two order's registration timestamp
     */
    @Override
    public int compareTo(CustomerOrder order) {

        int diff = 0;
        if (this.expectedDeliveryTime == null || order.expectedDeliveryTime == null) {
            diff = Double.compare(this.customerLocation.getDistanceFromCenter(), order.customerLocation.getDistanceFromCenter());
        }
        else {
            diff = this.expectedDeliveryTime.compareTo(order.expectedDeliveryTime);
        }

        return diff == 0 ? this.expectedDeliveryTime.compareTo(order.expectedDeliveryTime) : diff;
    }
}
