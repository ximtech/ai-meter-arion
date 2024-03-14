package aimeter.arion.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AIMeterData {

    @Id
    Long id;
    LocalDateTime dateCreated;
    
    String mimeType;
    String imageName;
    long imageSize;
    byte[] imageData;
    LocalDateTime imageDate;
    BigDecimal reading;
    
    Long meterId;
}