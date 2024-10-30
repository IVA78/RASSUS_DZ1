package hr.fer.tel.rassus.server.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="reading")
public class ReadingEntity {

    @Id
    @Column(name="identifier")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long identifier;

    @Column(name="sensorIdentifier", nullable = false)
    private Long sensorIdentifier;
    @Column(name="timestamp", nullable = false)
    private LocalDateTime timestamp;
    @Column(name="temperature", nullable = false)
    private Integer temperature;
    @Column(name="pressure", nullable = false)
    private Integer pressure;
    @Column(name="humidity", nullable = false)
    private Integer humidity;
    @Column(name="co", nullable = false)
    private Integer co;
    @Column(name="no2", nullable = false)
    private Integer no2;
    @Column(name="so2", nullable = false)
    private Integer so2;
}
