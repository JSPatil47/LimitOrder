package org.afob.limit;

import org.afob.execution.ExecutionClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class LimitOrderAgentTest {
    public interface ExecutionClient {
        void buy(String productId, int amount);
        void sell(String productId, int amount);
    }

    private static class StubExecutionClient implements ExecutionClient {
        private String lastProductId;
        private int lastAmount;

        @Override
        public void buy(String productId, int amount) {
            lastProductId = productId;
            lastAmount = amount;
        }

        @Override
        public void sell(String productId, int amount) {
            lastProductId = productId;
            lastAmount = amount;
        }

        public String getLastProductId() {
            return lastProductId;
        }

        public int getLastAmount() {
            return lastAmount;
        }
    }

    private LimitOrderAgent limitOrderAgent;
    private StubExecutionClient stubExecutionClient;

    @Before
    public void setUp() {
        stubExecutionClient = new StubExecutionClient();
        //limitOrderAgent = new LimitOrderAgent(stubExecutionClient);
    }

}
