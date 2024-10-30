package hr.fer.tel.rassus.server.services;

import hr.fer.tel.rassus.server.beans.SensorDTO;
import hr.fer.tel.rassus.server.entity.SensorEntity;
import hr.fer.tel.rassus.server.repository.SensorRepository;
import hr.fer.tel.rassus.server.utils.IdentifierGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    public List<SensorDTO> getAll() {
        List<SensorEntity> sensorEntityList = sensorRepository.findAll();
        return sensorEntityList.stream().map(
                sensorEntity -> new SensorDTO(sensorEntity.getLatitude(), sensorEntity.getLongitude(),sensorEntity.getIp(), sensorEntity.getPort())
        ).collect(Collectors.toList());

    }

    public SensorDTO findClosestNeighbour(String identifier) {

        SensorEntity currentSensor = sensorRepository.findByIdentifier(identifier);

        if(currentSensor == null) {
            return null;
        }

        List<SensorDTO> allSensors = getAll();

        if(allSensors.isEmpty()) {
            return null;
        }

        Map<SensorDTO, Double> distances = new HashMap<>();

        for(SensorDTO sensorDTO : allSensors) {
            if (sensorDTO.getLatitude().equals(currentSensor.getLatitude()) &&
                    sensorDTO.getLongitude().equals(currentSensor.getLongitude()) &&
                    sensorDTO.getIp().equals(currentSensor.getIp()) &&
                    sensorDTO.getPort().equals(currentSensor.getPort())) {
                continue;
            } else {

                Double lon1 = Math.toRadians(currentSensor.getLongitude());
                Double lon2 = Math.toRadians(sensorDTO.getLongitude());
                Double lat1 = Math.toRadians(currentSensor.getLatitude());
                Double lat2 = Math.toRadians(sensorDTO.getLatitude());

                Integer R = 6371; // Radius of Earth in kilometers

                Double dlon = lon2 - lon1;
                Double dlat = lat2 - lat1;
                Double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
                Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                Double d = R * c;

                distances.put(sensorDTO, d);
            }
        }

        if(distances.isEmpty()){
            return null;
        }

        Double minDistance = Collections.min(distances.values());
        Optional<SensorDTO> sensorDTO = distances.entrySet().stream().filter(entry -> entry.getValue().equals(minDistance)).map(Map.Entry::getKey).findFirst();

        if(sensorDTO.isPresent()) {
            return sensorDTO.get();
        } else {
            return null;
        }

    }

}
