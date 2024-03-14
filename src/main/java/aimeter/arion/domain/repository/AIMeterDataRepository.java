package aimeter.arion.domain.repository;

import aimeter.arion.domain.entity.AIMeterData;
import org.springframework.data.repository.CrudRepository;

public interface AIMeterDataRepository extends CrudRepository<AIMeterData, Long> {
    
}
