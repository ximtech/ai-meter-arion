package aimeter.arion.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class AIMeterDevice {

    @Id
    long id;
    String deviceName;
    int batteryLevel;

}
