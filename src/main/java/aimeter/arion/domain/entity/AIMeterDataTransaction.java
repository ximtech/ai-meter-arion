package aimeter.arion.domain.entity;

import aimeter.arion.domain.AIMeterTransactionStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
public class AIMeterDataTransaction {

    @Id
    long id;
    LocalDateTime dateCreated = LocalDateTime.now();
    AIMeterTransactionStatus status;
    String message;
    
    long dataId;
    long subscriptionId;

    public AIMeterDataTransaction(AIMeterTransactionStatus status, long dataId, long subscriptionId) {
        this.status = status;
        this.dataId = dataId;
        this.subscriptionId = subscriptionId;
    }
}
