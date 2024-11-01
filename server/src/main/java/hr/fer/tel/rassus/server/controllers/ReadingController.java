package hr.fer.tel.rassus.server.controllers;


import hr.fer.tel.rassus.server.beans.ReadingDTO;
import hr.fer.tel.rassus.server.services.ReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/readings")
public class ReadingController {

    @Autowired
    private ReadingService readingService;

  // spremanje očitanja pojedinog senzora
    @PostMapping("/saveReading/{identifier}")
    public ResponseEntity<Void> saveReading(@PathVariable("identifier") String sensorIdentifier, @RequestBody ReadingDTO readingDTO) {
        Boolean saved = readingService.saveReading(sensorIdentifier, readingDTO);

        if(saved) {
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("Reading saved."));

            return new ResponseEntity<>(headers, HttpStatus.CREATED);

        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


  //popis očitanja pojedinog senzora
  @GetMapping("/getReadings/{identifier}")
  public List<ReadingDTO>  getReadings(@PathVariable("identifier") String sensorIdentifier) {
      List<ReadingDTO> readingDTOList = readingService.getAllReadings(sensorIdentifier);
      return readingDTOList;
  }

}