package aimeter.arion.domain.repository;

import aimeter.arion.domain.entity.AIMeterDevice;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.NoSuchElementException;
import java.util.Optional;

public interface AIMeterDeviceRepository extends CrudRepository<AIMeterDevice, Long> {
    
    @Query("""
        SELECT * FROM ai_meter_config AS config
        JOIN ai_meter_device amd ON config.id = amd.config_id
        WHERE amd.id = :id AND amd.registered IS TRUE""")
    Optional<AIMeterDevice> findRegisteredAIMeterDevice(@Param("id") long id);
    
    default AIMeterDevice findRegisteredAIMeterDeviceOrThrow(long id) {
        return findRegisteredAIMeterDevice(id)
                .orElseThrow(() -> new NoSuchElementException("Meter with id: [%d] is not exist or not registered".formatted(id)));
    }
}
