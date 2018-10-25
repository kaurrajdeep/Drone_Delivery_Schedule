package com.company.domain;

/**
 * @author rajdeep kaur
 */
public class Time implements Comparable<Time>{

    private int seconds;

    /**
     * Constructor that creates time object based on total seconds since midgnight
     * @param seconds total seconds count
     */
    public Time(int seconds) {
        this.seconds = seconds;
    }

    /**
     * Constructor that creates timestamp based on hour, minute, and second input
     * @param hour
     * @param minute
     * @param second
     */
    public Time(int hour, int minute, int second) {
        this.seconds = hour * 3600 + minute * 60 + second;
    }

    /**
     * Getter method for total seconds count
     * @return total seconds count
     */
    public int getSeconds() {
        return seconds;
    }

    /**
     * Updates time
     * @param seconds add seconds
     */
    public void add(int seconds) {
        this.seconds += seconds;
    }

    /**
     * To print back in the format input was given
     * @return
     */
    @Override
    public String toString() {
        int hour = seconds / 3600;
        int minute = (seconds % 3600) / 60;
        int second = seconds % 60;

        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    @Override
    public int compareTo(Time timestamp) {
        return Integer.compare(this.seconds, timestamp.seconds);
    }

}
