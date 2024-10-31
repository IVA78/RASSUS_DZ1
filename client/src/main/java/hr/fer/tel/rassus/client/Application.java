package hr.fer.tel.rassus.client;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Random;

public class Application {

  private static String server_url = "http://localhost:8090";
  private static String identifier = null;
  private static ObjectMapper objectMapper = new ObjectMapper();
  private static HttpClient client = HttpClient.newHttpClient();

  public static Boolean register(Double latitude, Double longitude, String ip, Integer port) {

    var requestBody = Map.of(
            "latitude", latitude,
            "longitude", longitude,
            "ip", ip,
            "port", port
    );

    String json;
    try{
      json = objectMapper.writeValueAsString(requestBody);
    } catch (Exception e) {
      throw new RuntimeException("Pogreška pri serijaliziranju zahtjeva u JSON format!");
    }

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(server_url+ "/server/registerSensor"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();

    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      System.out.println("Statusni kod: " + response.statusCode());
      if (response.statusCode() == 201) {
        identifier = response.headers().firstValue("Location").orElse(null);
        return identifier != null;
      } else {
        throw new RuntimeException("Neuspješna registracija, statusni kod: " + response.statusCode());
      }
    } catch (Exception e) {
      System.err.println("Pogreška: " + e.getMessage());
      return false;
    }

  }

  public static Double generateRandomDouble(Double min, Double max) {
    Random random = new Random();
    return min + (max - min) * random.nextDouble();
  }

  public static Integer generateRandomInteger(Integer min, Integer max) {
    Random random = new Random();
    return min + random.nextInt(max - min);
  }



  public static void main(String[] args) {

    //registracija na poslužitelju
    Double longitude = generateRandomDouble(15.87, 16.0);
    Double latitude = generateRandomDouble(45.75, 45.85);
    String ip= "127.0.0.1";
    Integer port = generateRandomInteger(8000, 65000);

    //provjera je li postavljen identifikator
    Boolean registered = register(latitude, longitude, ip, port);
    System.out.println("Sensor is registered: " + registered);
    System.out.println("Identifier: "+ identifier);



  }

}
