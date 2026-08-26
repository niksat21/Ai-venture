package com.governance.controller;

import com.governance.dto.*;
import com.governance.service.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/governance")
public class GovernanceController {

    private final ComplianceService complianceService;
    private final ModelRoutingEngine modelRoutingEngine;
    private final ApplicationEventPublisher eventPublisher;

    public GovernanceController(ComplianceService complianceService,
                                ModelRoutingEngine modelRoutingEngine,
                                ApplicationEventPublisher eventPublisher) {
        this.complianceService = complianceService;
        this.modelRoutingEngine = modelRoutingEngine;
        this.eventPublisher = eventPublisher;
    }

    @PostMapping("/evaluate")
    public EvaluationResponse evaluateRequest(@RequestBody EvaluationRequest request) {
        // Step 1: Pre-Execution Scan
        List<String> violations = complianceService.inspectPayload(request);
        boolean allowed = violations.isEmpty();

        // Step 2: Fire-and-forget telemetry stream dispatch
        String auditMessage = allowed ? "Payload verified safe." : "Blocked via Policy Engine: " + violations;
        eventPublisher.publishEvent(new AuditEvent(request.agentId(), "INLINE_INTERCEPT", allowed, auditMessage));

        // Step 3: Resilient Cluster Execution Circuit Execution
        String modelOutput = "BLOCKED_BY_GOVERNANCE_GATEWAY_POLICY";
        if (allowed) {
            modelOutput = modelRoutingEngine.executeInferenceWithFailover(request.userPrompt());
        }

        return new EvaluationResponse(allowed, violations, modelOutput);
    }
}
