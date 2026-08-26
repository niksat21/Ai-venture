# AIAgent-Control | Inline AI Agent Governance & Resilient Gateway

<img width="792" height="708" alt="image" src="https://github.com/user-attachments/assets/cc2f0608-ade0-4851-b55c-03617f61726b" />


**AIGov-Control** is a model-agnostic, enterprise-grade AI compliance gateway and control plane built on Java 21 and Spring Boot 3. It serves as an architectural blueprint simulating the core telemetry, security interceptors, and asset policy controls found within **AI Control Tower (ai)**.

## 🏛️ Core Architectural Foundations

The engine sits as an inline reverse-proxy between client-facing AI Agent ecosystems and backend reasoning nodes (such as local open-source deployments or cloud clusters).

1. **Pre-Execution Scanning Engine**: Inspects inbound request bodies for adversarial overrides, prompt injections, and scope-access boundary compliance before forwarding payloads to downstream inference layers.
2. **Asynchronous Non-Blocking Telemetry Bus**: Broadcasts immutable audit traces over an internal Spring Event Bus, preserving system response speeds while meeting compliance requirements.
3. **Active-Passive High-Availability Routing**: Features an active circuit-breaker routing model. If downstream components go offline, the gateway catches the exception, updates performance metrics, and shifts to a hot fallback engine without interrupting operations.
4. **Cloud-Native Production Observability**: Exposes real-time health data via Micrometer and Spring Boot Actuator, ready to plug directly into Prometheus/Grafana clusters.

## 🚀 Execution & Quickstart Loops

### Prerequisites
* Java 21 JDK & Maven 3.x
* [Ollama](https://ollama.com) running a local model instance (`ollama run llama3`)

### Local Startup Pipeline
```bash
# 1. Clone and compile the application artifacts
git clone https://github.com
cd AIGov-control
mvn clean package

# 2. Run the platform boot sequence
java -jar target/AIGov-control-1.0.0.jar
```

## 📈 Real-Time Production Metrics Available
The system provides production telemetry tracking data directly at `http://localhost:8080/actuator/prometheus`:
* `ai_gateway_inference_success_total`: Total healthy completions.
* `ai_gateway_inference_latency_seconds`: Latency distributions across the model tier.
* `ai_gateway_circuit_tripped_total`: Total failover events triggered by downstream outges.
