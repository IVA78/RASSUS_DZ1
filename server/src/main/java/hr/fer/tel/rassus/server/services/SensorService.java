package hr.fer.tel.rassus.server.services;

import hr.fer.tel.rassus.server.beans.SensorDTO;
import hr.fer.tel.rassus.server.entity.SensorEntity;
import hr.fer.tel.rassus.server.repository.SensorRepository;
import hr.fer.tel.rassus.server.utils.IdentifierGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SensorService {

    @Autowired
    private SensorRepository sensorRepository;

    public String register(SensorDTO sensorDTO) {
        try {
            //generiranje identifikatora
            String identifier = IdentifierGenerator.generateIdentifier();
            //definiranje novog senzora
            SensorEntity newSensor = new SensorEntity(identifier, sensorDTO.getLatitude(), sensorDTO.getLongitude(), sensorDTO.getIp(), sensorDTO.getPort());
            //spremanje podataka o senzoru u h2 in-memory bazu
            sensorRepository.save(newSensor);
            return identifier;

        } catch(Exception e) {
            e.printStackTrace();
            return null;

        }
    }

}
