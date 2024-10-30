package hr.fer.tel.rassus.server.services;

import hr.fer.tel.rassus.server.beans.ReadingDTO;
import hr.fer.tel.rassus.server.beans.SensorDTO;
import hr.fer.tel.rassus.server.entity.ReadingEntity;
import hr.fer.tel.rassus.server.entity.SensorEntity;
import hr.fer.tel.rassus.server.repository.ReadingRepository;
import hr.fer.tel.rassus.server.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReadingService {

    @Autowired
    SensorRepository sensorRepository;

    @Autowired
    ReadingRepository readingRepository;

    public boolean saveReading(String sensorIdentifier, ReadingDTO readingDTO) {
        SensorEntity sensor = sensorRepository.findByIdentifier(sensorIdentifier);
        if(sensor == null) {
            return false;
        }

        try {
            ReadingEntity newReading = new ReadingEntity();
            newReading.setSensorIdentifier(sensorIdentifier);
            newReading.setTimestamp(LocalDateTime.now());
            newReading.setTemperature(readingDTO.getTemperature());
            newReading.setPressure(readingDTO.getPressure());
            newReading.setHumidity(readingDTO.getHumidity());
            newReading.setCo(readingDTO.getCo());
            newReading.setNo2(readingDTO.getNo2());
            newReading.setSo2(readingDTO.getSo2());

            readingRepository.save(newReading);

            return true;

        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ReadingDTO> getAllReadings(String sensorIdentifier) {

        SensorEntity sensor = sensorRepository.findByIdentifier(sensorIdentifier);

        if(sensor == null) {
            return null;
        }

        List<ReadingEntity> readingEntityList = readingRepository.findBySensorIdentifier(sensorIdentifier);

        return readingEntityList.stream().map(
                readingEntity -> new ReadingDTO(readingEntity.getTemperature(), readingEntity.getPressure(),readingEntity.getHumidity(), readingEntity.getCo(), readingEntity.getNo2(), readingEntity.getSo2())
        ).collect(Collectors.toList());

    }
}
