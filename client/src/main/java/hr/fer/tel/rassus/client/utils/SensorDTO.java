package hr.fer.tel.rassus.client.utils;

/**
 * This class represents a sensor data transfer object (DTO)
 * It contains sensor-related information and provides
 * methods for sensor operations.
 */
public class SensorDTO {
    private Double latitude;
    private Double longitude;
    private String ip;
    private Integer port;

    public SensorDTO() {
    }

    public SensorDTO( Double latitude, Double longitude, String ip, Integer port) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.ip = ip;
        this.port = port;
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

