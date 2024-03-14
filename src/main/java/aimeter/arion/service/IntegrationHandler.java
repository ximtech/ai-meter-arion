package aimeter.arion.service;

import aimeter.arion.dto.IntegrationContext;

public interface IntegrationHandler {
    
    String integrationType();

    void runIntegration(IntegrationContext context);
    
}
