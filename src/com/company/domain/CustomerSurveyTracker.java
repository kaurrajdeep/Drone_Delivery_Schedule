package com.company.domain;

/**
 * @author rajdeep kaur
 */
public class CustomerSurveyTracker {
    public enum CustomerType {
        DETRACTOR, NEUTRAL, PROMOTER
    }
    private int neutralThreshold;
    private int detractorThreshold;

    private int feedbackCount;
    private int promoter;
    private int detractor;

    /**
     * Default constructor
     * @param neutralThreshold the delivery time threshold for neutral feedback
     * @param detractorThreshold the delivery time threshold for detractor feedback
     */
    public CustomerSurveyTracker (int neutralThreshold, int detractorThreshold) {
        this.neutralThreshold = neutralThreshold;
        this.detractorThreshold = detractorThreshold;

        this.feedbackCount = 0;
        this.promoter = 0;
        this.detractor = 0;
    }

    public int getDetractor() {
        return this.detractor;
    }

    public void setDetractor(int detractor) {
        this.detractor = detractor;
    }

    /**
     * Updates the metric based on input delivery time
     * @param time the time to deliver a specific order
     */
    public void update(int time) {
        if (time < neutralThreshold) {
            promoter++;
        }
        else if (time >= detractorThreshold) {
            detractor++;
        }

        feedbackCount++;
    }

    /**
     * Check the satisfaction level of the input delivery time
     * @param time the time to deliver a specific order
     * @return the satisfaction level of the given delivery time
     */
    public CustomerType getSatisfaction(int time) {
        if (time < neutralThreshold) {
            return CustomerType.PROMOTER;
        }
        else if (time < detractorThreshold) {
            return CustomerType.NEUTRAL;
        }
        else {
            return CustomerType.DETRACTOR;
        }
    }

    /**
     * Get the net promoter score based tracked metrics
     * @return NPS
     */
    public double getNPS() {
        if (feedbackCount == 0) return 0;
        return (promoter / (double) feedbackCount - detractor / (double) feedbackCount) * 100;
    }
}
