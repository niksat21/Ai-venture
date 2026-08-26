package com.governance.service;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ModelRoutingEngineTest {

    private ModelRoutingEngine routingEngine;

    @BeforeEach
    void setUp() {
        // Use a SimpleMeterRegistry to test metrics output without spinning up a full, slow application context
        this.routingEngine = new ModelRoutingEngine(new SimpleMeterRegistry());
    }

    @Test
    @DisplayName("Should successfully trip Circuit Breaker and deploy Hot Failover when Downstream Ollama goes down")
    void testCircuitBreakerAndFailoverExecution() {
        String testPrompt = "Execute high-risk transaction data migrations.";

        // Call the service while Ollama is intentionally disconnected to simulate a network outage
        String response = routingEngine.executeInferenceWithFailover(testPrompt);

        // Verify the gateway intercepts the breakdown cleanly and executes the hot passive backup logic
        assertTrue(response.contains("HOT_PASSIVE_BACKUP_CIRCUIT"),
                "The gateway did not catch the network drop and leaked a system error.");
        assertTrue(response.contains(testPrompt),
                "The fallback engine failed to pass the original query context through the fallback loop.");
    }
}
