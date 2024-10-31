package hr.fer.tel.rassus.server.entity;

import jakarta.persistence.*;

@Entity
@Table(name="sensor")
public class SensorEntity {
    @Id
    @Column(name="identifier", nullable = false, unique = true)
    private String identifier;

    @Column(name="latitude", nullable = false)
    private Double latitude;
    @Column(name="longitude", nullable = false)
    private Double longitude;
    @Column(name="ip", nullable = false)
    private String ip;
    @Column(name="port", nullable = false)
    private Integer port;

    public SensorEntity() {
    }

    public SensorEntity(String identifier, Double latitude, Double longitude, String ip, Integer port) {
        this.identifier = identifier;
        this.latitude = latitude;
        this.longitude = longitude;
        this.ip = ip;
        this.port = port;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
