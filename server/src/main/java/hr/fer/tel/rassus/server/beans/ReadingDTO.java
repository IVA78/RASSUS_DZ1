package hr.fer.tel.rassus.server.beans;

import java.time.LocalDateTime;

/**
 * This class represents a reading data transfer object (DTO)
 * It contains reading-related information and provides
 * methods for sensor operations.
 */
public class ReadingDTO {

  private Integer temperature;
  private Integer pressure;
  private Integer humidity;
  private Integer co;
  private Integer no2;
  private Integer so2;

    public ReadingDTO() {
    }

    public ReadingDTO( Integer temperature, Integer pressure, Integer humidity, Integer co, Integer no2, Integer so2) {
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.co = co;
        this.no2 = no2;
        this.so2 = so2;
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

