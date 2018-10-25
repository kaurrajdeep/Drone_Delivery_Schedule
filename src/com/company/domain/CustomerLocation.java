package com.company.scheduler;
import com.company.domain.CustomerOrder;
import com.company.domain.CustomerReceipt;
import com.company.domain.CustomerSurveyTracker;
import com.company.domain.Time;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Scheduler class that takes input order queue and construct a delivery scheduler that optimizes NPS
 */
public class OrderScheduler {

    private Time currentTimestamp;
    private Time end;
    private int secPerUnit;
    private CustomerSurveyTracker metric;

    /**
     * Default constructor
     * @param start the operation start time
     * @param end the operation end time
     * @param secPerUnit drone travel time per unit in seconds
     * @param metric the satisfaction metric
     */
    public OrderScheduler(Time start, Time end, int secPerUnit, CustomerSurveyTracker metric) {
        this.currentTimestamp = start;
        this.end = end;
        this.secPerUnit = secPerUnit;
        this.metric = metric;
    }

    /**
     * Takes the input order queue and generates corresponding receipt list
     * @param orderQueue the order queue that consists of all the orders
     * @return list of receipts with optimal scheduling
     */
    public List<CustomerReceipt> generateReceipts(Queue<CustomerOrder> orderQueue) {
        List<CustomerReceipt> receiptList = new LinkedList<>();
        PriorityQueue<CustomerOrder> readyQueue = new PriorityQueue<>();
        PriorityQueue<CustomerOrder> slowQueue = new PriorityQueue<>();

        while (currentTimestamp.compareTo(end) < 0 && (!orderQueue.isEmpty() || !readyQueue.isEmpty())) {

            if (!orderQueue.isEmpty()) {
                readyQueue = updateQueue(readyQueue, slowQueue);
            }

            while (!orderQueue.isEmpty()) {
                CustomerOrder order = orderQueue.peek();

                Time expectedTime = new Time(currentTimestamp.getSeconds());
                expectedTime.add(getTravelTime(order));
                if (metric.getSatisfaction(this.getTravelTime(order)) == CustomerSurveyTracker.CustomerType.DETRACTOR) {
                    order.setExpectedDeliveryTime(expectedTime);
                    slowQueue.add(order);
                    orderQueue.remove();
                }
                else if (order.getOrderPlacedTime().compareTo(currentTimestamp) <= 0) {
                    order.setExpectedDeliveryTime(expectedTime);
                    readyQueue.add(order);
                    orderQueue.remove();
                }
                else {
                    break;
                }
            }

            if (readyQueue.isEmpty()) {
                if (orderQueue.isEmpty()) {
                    break;
                }
                CustomerOrder nextOrder = orderQueue.peek();
                if (!slowQueue.isEmpty()) {
                    CustomerOrder delivery = slowQueue.peek();
                    int nextAvailableTime = currentTimestamp.getSeconds() + getTravelTime(delivery);
                    if (nextAvailableTime <= nextOrder.getOrderPlacedTime().getSeconds()) {
                        updateSchedulerStatus(delivery, receiptList);
                        slowQueue.remove();
                        continue;
                    }
                }
                currentTimestamp = new Time(nextOrder.getOrderPlacedTime().getSeconds());
            }
            else {
                CustomerOrder delivery = readyQueue.remove();
                updateSchedulerStatus(delivery, receiptList);
            }
        }

        while (currentTimestamp.compareTo(end) < 0 && (!slowQueue.isEmpty())) {
            CustomerOrder delivery = slowQueue.remove();
            updateSchedulerStatus(delivery, receiptList);
        }

        metric.setDetractor(metric.getDetractor() + readyQueue.size() + slowQueue.size());
        return receiptList;
    }

    /**
     * Helper method to get the order travel time for delivery
     * @param order the input order
     * @return the order travel time for delivery
     */
    private int getTravelTime(CustomerOrder order) {
        return (int) Math.ceil(order.getCustomerLocation().getDistanceFromCenter() * secPerUnit * 2);
    }

    /**
     * Helper method to update the metric and receipt list with the given input order
     * @param order the input order for update
     * @param receiptList the list that stores the receipts
     */
    private void updateSchedulerStatus(CustomerOrder order, List<CustomerReceipt> receiptList) {
        CustomerReceipt receipt = new CustomerReceipt(order.getIdentifier(),
                new Time(currentTimestamp.getSeconds()));
        receiptList.add(receipt);

        metric.update(order.expectedWaitTime());
        currentTimestamp.add(getTravelTime(order));
    }

    /**
     * Helper method which reorder the ready queue and slow queue base on the current timestamp
     * @param readyQueue the queue with all the orders ready to delivery
     * @param slowQueue the queue with all the orders that contains detractor satisfaction
     * @return the updated ready queue based on the current timestamp
     */
    private PriorityQueue<CustomerOrder> updateQueue(PriorityQueue<CustomerOrder> readyQueue, PriorityQueue<CustomerOrder> slowQueue) {
        PriorityQueue<CustomerOrder> temp = new PriorityQueue();

        while (!readyQueue.isEmpty()) {
            CustomerOrder order = readyQueue.remove();

            Time expectedTime = new Time(currentTimestamp.getSeconds());
            expectedTime.add(getTravelTime(order));
            order.setExpectedDeliveryTime(expectedTime);

            if (metric.getSatisfaction(order.expectedWaitTime()) == CustomerSurveyTracker.CustomerType.DETRACTOR) {
                slowQueue.add(order);
            }
            else {
                temp.add(order);
            }
        }

        return temp;
    }
}
