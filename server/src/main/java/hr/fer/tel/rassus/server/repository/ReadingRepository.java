package hr.fer.tel.rassus.server.repository;

import hr.fer.tel.rassus.server.entity.ReadingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReadingRepository extends JpaRepository<ReadingEntity, Long> {
  Optional<ReadingEntity> findById(Long identifier);

  ReadingEntity findBySensorIdentifier(Long sensorIdentifier);
}
