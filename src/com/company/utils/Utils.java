package com.company.utils;
import com.company.domain.CustomerLocation;
import com.company.domain.CustomerOrder;
import com.company.domain.CustomerReceipt;
import com.company.domain.Time;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author rajdeep kaur
 */
public class Utils {

    /**
     * Utility method to parse the input file and generates a order queue
     * @param inputFile the input file
     * @return order queue
     * @throws IOException exception that occurred when parse input file
     */
    public static Queue<CustomerOrder> parseInputFile(File inputFile) throws IOException {
        BufferedReader inputFileReader = new BufferedReader(new FileReader(inputFile));

        String currentLine = "";
        Queue<CustomerOrder> orderList = new LinkedList<>();

        while ((currentLine = inputFileReader.readLine()) != null) {
            currentLine = currentLine.trim();

            if (currentLine.length() == 0) continue;

            String[] orderInfo = currentLine.split("\\s+");
            String orderIdentifier = orderInfo[0];

            int x = 0, y = 0;
            String northOrSouth ="N", eastOrWest="S";
            //validateLocation(orderInfo[1])else continue;
            if (orderInfo[1].charAt(0) =='N'|| orderInfo[1].charAt(0) =='S') {
                northOrSouth = Character.toString(orderInfo[1].charAt(0));
                int secondDirection = orderInfo[1].indexOf("E");
                if (secondDirection == -1)
                    secondDirection = orderInfo[1].indexOf("W");
                eastOrWest = Character.toString(orderInfo[1].charAt(secondDirection));
                x = Integer.parseInt(orderInfo[1].substring(1,secondDirection));
                y = Integer.parseInt(orderInfo[1].substring(secondDirection+1));
            }

            CustomerLocation location = new CustomerLocation(x, y, northOrSouth, eastOrWest);

            String[] timeInfo = orderInfo[2].split(":");
            int hour = Integer.parseInt(timeInfo[0]);
            int minute = Integer.parseInt(timeInfo[1]);
            int second = Integer.parseInt(timeInfo[2]);
            //validateTime(timeInfo) else continue;
            Time timestamp = new Time(hour, minute, second);

            CustomerOrder order = new CustomerOrder(orderIdentifier, location, timestamp);
            orderList.add(order);
        }

        inputFileReader.close();

        return orderList;
    }

    /**
     * Utility method which takes input list of receipts and NPS and generates an output file
     * @param fileName the name of the output to be generated
     * @param receiptList the list of receipts
     * @param nps the NPS
     */
    public static void generateReceiptFile(String fileName, List<CustomerReceipt> receiptList, double nps) {
        try {
            Writer fileWriter = new FileWriter(fileName);
            for (CustomerReceipt receipt : receiptList) {
                fileWriter.write(receipt.toString() + "\n");
            }
            fileWriter.write("NPS " + nps);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Fail to write to output file: " + e.getMessage());
        }
    }
}
