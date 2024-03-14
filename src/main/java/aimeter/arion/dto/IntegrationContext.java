package aimeter.arion.dto;

import aimeter.arion.domain.entity.AIMeterData;
import aimeter.arion.domain.entity.AIMeterDataTransaction;
import aimeter.arion.domain.entity.AIMeterDevice;
import aimeter.arion.domain.entity.AIMeterSubscription;

public record IntegrationContext(
        AIMeterDataTransaction dataTransaction,
        AIMeterDevice device,
        AIMeterSubscription subscription,
        AIMeterData data
) {
}
