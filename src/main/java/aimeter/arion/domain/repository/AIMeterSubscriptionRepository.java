package aimeter.arion.domain.repository;

import aimeter.arion.domain.entity.AIMeterSubscription;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AIMeterSubscriptionRepository extends CrudRepository<AIMeterSubscription, Long> {
    
    List<AIMeterSubscription> findAIMeterSubscriptionByMeterId(long meterId);
    
}
