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
    private String sensorIdentifier;
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
    @Column(name="no2")
    private Integer no2;
    @Column(name="so2")
    private Integer so2;

    public ReadingEntity() {
    }

    public ReadingEntity(Long identifier, String sensorIdentifier, LocalDateTime timestamp, Integer temperature, Integer pressure, Integer humidity, Integer co, Integer no2, Integer so2) {
        this.identifier = identifier;
        this.sensorIdentifier = sensorIdentifier;
        this.timestamp = timestamp;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.co = co;
        this.no2 = no2;
        this.so2 = so2;
    }

    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    public String getSensorIdentifier() {
        return sensorIdentifier;
    }

    public void setSensorIdentifier(String sensorIdentifier) {
        this.sensorIdentifier = sensorIdentifier;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public Integer getPressure() {
        return pressure;
    }

    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Integer getCo() {
        return co;
    }

    public void setCo(Integer co) {
        this.co = co;
    }

    public Integer getNo2() {
        return no2;
    }

    public void setNo2(Integer no2) {
        this.no2 = no2;
    }

    public Integer getSo2() {
        return so2;
    }

    public void setSo2(Integer so2) {
        this.so2 = so2;
    }
}
