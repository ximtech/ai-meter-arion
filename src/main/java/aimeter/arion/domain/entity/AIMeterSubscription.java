package aimeter.arion.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AIMeterSubscription {
    
    @Id
    long id;
    LocalDateTime dateCreated;
    LocalDateTime dateUpdated;
    String subscriptionType;
    
    Long meterId;
    String description;
    Long telegramChatId;
}
