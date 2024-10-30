package hr.fer.tel.rassus.server.controllers;


import hr.fer.tel.rassus.server.beans.SensorDTO;
import hr.fer.tel.rassus.server.services.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/sensors")
public class SensorsController {

    @Autowired
    private SensorService sensorService;

    //registracija
    @PostMapping("/register")
    public ResponseEntity<Void> registerSensor(@RequestBody SensorDTO sensorDTO) {
        String identifier = sensorService.register(sensorDTO);

        if(identifier != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/sensors/"+identifier));
            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //  TODO 4.4  Popis senzora

    //  TODO 4.2  Najbliži susjed

}
