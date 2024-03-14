package aimeter.arion;

import aimeter.arion.domain.entity.AIMeterData;
import aimeter.arion.domain.repository.AIMeterDataRepository;
import aimeter.arion.dto.MeterDataQueueMessage;
import aimeter.arion.exception.IntegrationException;
import aimeter.arion.service.AIMeterIntegrationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.iron.ironmq.Message;
import io.iron.ironmq.Queue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@EnableScheduling
@SpringBootApplication
@RequiredArgsConstructor
public class AiMeterArionApplication {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Qualifier("meterDataQueue")
    final Queue meterDataQueue;
    final AIMeterDataRepository aiMeterDataRepository;
    final AIMeterIntegrationService aiMeterIntegrationService;

    public static void main(String[] args) {
        SpringApplication.run(AiMeterArionApplication.class, args);
    }

    @Scheduled(cron = "*/5 * * ? * *")  // Poll queue every 5 seconds
    public void handleIronMQMessage() {
        MeterDataQueueMessage dataQueueMessage = getMeterDataMessage().orElse(null);
        if (dataQueueMessage != null) {
            log.info("Received data message from queue with id: [{}]", dataQueueMessage.meterDataId());
            AIMeterData meterData = aiMeterDataRepository.findById(dataQueueMessage.meterDataId())
                    .orElseThrow(() -> new IntegrationException("Meter data with id: [%d] not found".formatted(dataQueueMessage.meterDataId())));
            aiMeterIntegrationService.processMeterDataIntegration(meterData);
        }
    }

    private Optional<MeterDataQueueMessage> getMeterDataMessage() {
        try {
            Message message = meterDataQueue.reserve();
            MeterDataQueueMessage dataQueueMessage = OBJECT_MAPPER.readValue(message.getBody(), MeterDataQueueMessage.class);
            meterDataQueue.deleteMessage(message);
            return Optional.of(dataQueueMessage);
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
