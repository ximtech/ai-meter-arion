package aimeter.arion.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MeterDataQueueMessage(
        @JsonProperty("meter_data_id") long meterDataId
) {
}
