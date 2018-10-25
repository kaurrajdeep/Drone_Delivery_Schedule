package com.company;

import com.company.domain.CustomerReceipt;
import com.company.domain.CustomerSurveyTracker;
import com.company.domain.Time;
import com.company.domain.CustomerOrder;
import com.company.scheduler.OrderScheduler;
import com.company.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Queue;

/**
 * @author rajdeep kaur
 */
public class Main {
    private final static Time START_TIME = new Time(6, 0, 0);
    private final static Time END_TIME = new Time(22, 0, 0);

    private final static int SECOND_PER_UNIT = 60;

    public static void main(String[] args) {
	// write your code here
        //check if input filepath is present

        if(args.length == 0)
        {
            System.out.println("Please pass input filepath as argument.");
            System.exit(0);
        }
        //read the input filename from command line
        File file = new File(args[0]); //"/Users/Apple/Desktop/test_orders.txt");
        Queue<CustomerOrder> orderQueue = null;
        try {

            orderQueue = Utils.parseInputFile(file);

            CustomerSurveyTracker metric = new CustomerSurveyTracker(5400, 12600);
            OrderScheduler scheduler = new OrderScheduler(START_TIME, END_TIME, SECOND_PER_UNIT, metric);

            List<CustomerReceipt> receiptList = scheduler.generateReceipts(orderQueue);
            double nps = metric.getNPS();
            Utils.generateReceiptFile("output.txt", receiptList, nps);
        } catch (IOException e) {
            System.out.println("Unable to parse file: " + e.getMessage());
        }
    }
}
