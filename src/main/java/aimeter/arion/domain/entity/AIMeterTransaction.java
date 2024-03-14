package aimeter.arion.domain.entity;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class AIMeterTransaction {

    @Id
    Long id;
    LocalDateTime dateCreated;

}
