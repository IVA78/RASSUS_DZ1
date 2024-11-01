package hr.fer.tel.rassus.server.controllers;


import hr.fer.tel.rassus.server.beans.SensorDTO;
import hr.fer.tel.rassus.server.services.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/server")
public class SensorsController {

    @Autowired
    private SensorService sensorService;

    //registracija
    @PostMapping("/registerSensor")
    public ResponseEntity<Void> registerSensor(@RequestBody SensorDTO sensorDTO) {
        String identifier = sensorService.register(sensorDTO);

        if (identifier != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("http://" + sensorDTO.getIp() + ":" + sensorDTO.getPort() + "Identifier:" + identifier));

            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // popis senzora
    @GetMapping("/getAllSensors")
    public List<SensorDTO> getAllSensors() {
        List<SensorDTO> sensorDTOList = sensorService.getAll();
        return sensorDTOList;
    }

    //najblizi susjed
    @GetMapping("/findClosestNeighbour/{identifier}")
    public ResponseEntity<SensorDTO> findClosestNeighbour(@PathVariable("identifier") String identifier) {
        SensorDTO closestNeighbour = sensorService.findClosestNeighbour(identifier);
        if (closestNeighbour != null) {
            return ResponseEntity.ok(closestNeighbour);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

}
