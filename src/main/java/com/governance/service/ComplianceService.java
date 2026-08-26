package com.governance.service;

import com.governance.dto.EvaluationRequest;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ComplianceService {

    // Matches standard malicious prompts designed to bypass target LLM safety layers
    private static final Pattern JAILBREAK_PATTERN =
            Pattern.compile("(?i)(ignore previous instructions|system override|act as developer mode|dan mode)");

    public List<String> inspectPayload(EvaluationRequest request) {
        List<String> violations = new ArrayList<>();

        // Guardrail 1: Input Threat Protection
        if (JAILBREAK_PATTERN.matcher(request.userPrompt()).find()) {
            violations.add("POLICY_VIOLATION: Prompt Injection / Adversarial override signature detected.");
        }

        // Guardrail 2: Identity Privilege Boundary Enforcement (Simulates CSDM entitlement structures)
        if ("root_access".equalsIgnoreCase(request.requestedScope()) && !request.agentId().startsWith("ADMIN-")) {
            violations.add("ACCESS_VIOLATION: Non-privileged agent requested an administrative execution scope.");
        }

        return violations;
    }
}
