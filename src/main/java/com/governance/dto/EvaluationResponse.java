package com.governance.dto;

import java.util.List;

public record EvaluationResponse(boolean allowed, List<String> violations, String modelOutput) {}