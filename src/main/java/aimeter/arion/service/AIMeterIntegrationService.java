package aimeter.arion.service;

import aimeter.arion.domain.AIMeterTransactionStatus;
import aimeter.arion.domain.entity.AIMeterData;
import aimeter.arion.domain.entity.AIMeterDataTransaction;
import aimeter.arion.domain.entity.AIMeterDevice;
import aimeter.arion.domain.entity.AIMeterSubscription;
import aimeter.arion.domain.repository.AIMeterDataRepository;
import aimeter.arion.domain.repository.AIMeterDataTransactionRepository;
import aimeter.arion.domain.repository.AIMeterDeviceRepository;
import aimeter.arion.domain.repository.AIMeterSubscriptionRepository;
import aimeter.arion.dto.IntegrationContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static aimeter.arion.domain.AIMeterTransactionStatus.COMPLETED;
import static aimeter.arion.domain.AIMeterTransactionStatus.FAILED;
import static aimeter.arion.domain.AIMeterTransactionStatus.IN_PROGRESS;

@Slf4j
@Service
public class AIMeterIntegrationService {
    
    final AIMeterDeviceRepository aiMeterDeviceRepository;
    final AIMeterSubscriptionRepository aiMeterSubscriptionRepository;
    final AIMeterDataTransactionRepository aiMeterDataTransactionRepository;
    final Map<String, IntegrationHandler> integrationHandlerMap;

    public AIMeterIntegrationService(AIMeterDeviceRepository aiMeterDeviceRepository,
                                     AIMeterSubscriptionRepository aiMeterSubscriptionRepository,
                                     AIMeterDataTransactionRepository aiMeterDataTransactionRepository,
                                     List<IntegrationHandler> integrationHandlers) {
        this.integrationHandlerMap = integrationHandlers.stream()
                .collect(Collectors.toMap(IntegrationHandler::integrationType, Function.identity()));
        this.aiMeterDeviceRepository = aiMeterDeviceRepository;
        this.aiMeterSubscriptionRepository = aiMeterSubscriptionRepository;
        this.aiMeterDataTransactionRepository = aiMeterDataTransactionRepository;
    }

    public void processMeterDataIntegration(AIMeterData data) {
        AIMeterDevice device = aiMeterDeviceRepository.findRegisteredAIMeterDeviceOrThrow(data.getMeterId());
        aiMeterSubscriptionRepository.findAIMeterSubscriptionByMeterId(data.getMeterId()).forEach((AIMeterSubscription subscription) -> {
            IntegrationHandler handler = integrationHandlerMap.get(subscription.getSubscriptionType());
            if (handler == null) {
                throw new RuntimeException("Unknown subscription type: [%s]".formatted(subscription.getSubscriptionType()));
            }

            AIMeterDataTransaction dataTransaction = new AIMeterDataTransaction(IN_PROGRESS, data.getId(), subscription.getId());
            aiMeterDataTransactionRepository.save(dataTransaction);
            IntegrationContext context = new IntegrationContext(dataTransaction, device, subscription, data);
            handleIntegrationProcess(context, handler);
            log.info("Integration finished with status: {}", dataTransaction.getStatus());
        });
    }

    private void handleIntegrationProcess(IntegrationContext context, IntegrationHandler handler) {
        try {
            handler.runIntegration(context);
            
        } catch (Exception e) {
            log.error("Integration failed!", e);
            context.dataTransaction().setStatus(FAILED);
            context.dataTransaction().setMessage(e.getMessage());
            aiMeterDataTransactionRepository.save(context.dataTransaction());
            return;
        }
        context.dataTransaction().setStatus(COMPLETED);
        aiMeterDataTransactionRepository.save(context.dataTransaction());
    }
}
