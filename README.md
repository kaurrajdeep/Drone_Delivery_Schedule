# Drone_Delivery_Schedule

Requirements:
Java 1.8

How to run:
 java -jar DroneOrderDelivery.jar path_to_input_file
 
Assumptions:
1) Drone delivers one order at a time and comes back to the center to pickup order again.
2) It does not matter if a customer order is never delivered, i.e; all undelivered orders for the day are automatically detractors.


Algorithm: Greedy approach w.r.t wait time for an order.
1) Read the input file and create a queue of orders.
2) First, sort the orders based on expecteddelaytime for a customer.
3) Deliver orders with least wait time among all avilable orders.
4) Depriortize order with more than 3.5 hours of delay time.
5) Update time after a delivery and continue.
6) After all orders with time less than 2.5 hours are delivered, go with remaining orders.
