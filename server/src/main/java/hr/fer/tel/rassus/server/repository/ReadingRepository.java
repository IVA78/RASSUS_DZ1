package hr.fer.tel.rassus.server.repository;

import hr.fer.tel.rassus.server.entity.ReadingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReadingRepository extends JpaRepository<ReadingEntity, Long> {

  @Query("SELECT r FROM ReadingEntity r WHERE r.sensorIdentifier = :sensorIdentifier")
  List<ReadingEntity> findBySensorIdentifier(@Param("sensorIdentifier") String sensorIdentifier);

}
