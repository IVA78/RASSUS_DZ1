package hr.fer.tel.rassus.server.repository;

import hr.fer.tel.rassus.server.entity.SensorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends JpaRepository<SensorEntity, Long> {
    @Query("SELECT s FROM SensorEntity s WHERE s.identifier = :identifier")
    SensorEntity findByIdentifier(@Param("identifier") Long identifier);
}
