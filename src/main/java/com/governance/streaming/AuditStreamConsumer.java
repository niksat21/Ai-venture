package com.governance.streaming;

import com.governance.dto.AuditEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.time.Instant;

@Component
public class AuditStreamConsumer {

    @Async
    @EventListener
    public void consumeLogStream(AuditEvent event) {
        // Formats data for logging targets like Splunk, OpenSearch, or System Logs
        System.out.printf("[AUDIT AMQP TELETEMRY STREAM] [%s] TargetAgent: %s | Status Verified: %b | Summary: %s%n",
                Instant.now(), event.agentId(), event.compliancePassed(), event.detail());
    }
}
