package com.governance.dto;

public record AuditEvent(String agentId, String action, boolean compliancePassed, String detail) {}