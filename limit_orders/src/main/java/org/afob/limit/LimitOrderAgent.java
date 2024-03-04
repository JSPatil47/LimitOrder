package org.afob.limit;

import org.afob.execution.ExecutionClient;
import org.afob.prices.PriceListener;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class LimitOrderAgent implements PriceListener {

    private final org.afob.limit.LimitOrderAgentTest.ExecutionClientMock executionClient;
    private final Map<String, Order> orders;

    public LimitOrderAgent(final org.afob.limit.LimitOrderAgentTest.StubExecutionClient ec) {
        this.executionClient = ec;
        this.orders = new HashMap<>();
    }

    @Override
    public void priceTick(String productId, BigDecimal price) throws ExecutionClient.ExecutionException {
        // Check if there are any orders for this product
        if (orders.containsKey(productId)) {
            Order order = orders.get(productId);
            BigDecimal limit = order.limit;

            // If the market price reaches or exceeds the limit, execute the order
            if ((order.buy && price.compareTo(limit) <= 0) || (!order.buy && price.compareTo(limit) >= 0)) {
                executeOrder(order);
            }
        }
    }

    // Method to add buy/sell orders
    public void addOrder(boolean buy, String productId, int amount, BigDecimal limit) {
        orders.put(productId, new Order(buy, productId, amount, limit));
    }

    // Method to execute an order
    private void executeOrder(Order order) throws ExecutionClient.ExecutionException {
        if (order.buy) {
            executionClient.buy(order.productId, order.amount);
        } else {
            executionClient.sell(order.productId, order.amount);
        }
        orders.remove(order.productId); // Remove the executed order
    }

    // Inner class representing an order
    private static class Order {
        boolean buy;
        String productId;
        int amount;
        BigDecimal limit;

        public Order(boolean buy, String productId, int amount, BigDecimal limit) {
            this.buy = buy;
            this.productId = productId;
            this.amount = amount;
            this.limit = limit;
        }
    }
}