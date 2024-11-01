package hr.fer.tel.rassus.client;


import com.fasterxml.jackson.databind.ObjectMapper;
import hr.fer.tel.rassus.client.rpc.RPCClient;
import hr.fer.tel.rassus.client.rpc.RPCServer;
import hr.fer.tel.rassus.client.rpc.RPCService;
import hr.fer.tel.rassus.client.utils.ReadingDTO;
import hr.fer.tel.rassus.client.utils.SensorDTO;
import hr.fer.tel.rassus.client.utils.Utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Random;

public class Application {

    private static String server_url = "http://localhost:8090";
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static HttpClient client = HttpClient.newHttpClient();
    private static String identifier = null;
    private static ReadingDTO sensorReading = null;

    public static ReadingDTO getSensorReading() {
        return sensorReading;
    }

    public static Boolean register(Double latitude, Double longitude, String ip, Integer port) {

        var requestBody = Map.of(
                "latitude", latitude,
                "longitude", longitude,
                "ip", ip,
                "port", port
        );

        String json;
        try {
            json = objectMapper.writeValueAsString(requestBody);
        } catch (Exception e) {
            throw new RuntimeException("Pogreška pri serijaliziranju zahtjeva u JSON format!");
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(server_url + "/server/registerSensor"))
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

    public static SensorDTO findClosestNeighbour() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(server_url + "/server/findClosestNeighbour/" + identifier))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Status code: " + response.statusCode());

            if (response.statusCode() == 200) {
                // If the response is 200, parse the body as SensorDTO
                return objectMapper.readValue(response.body(), SensorDTO.class);
            } else if (response.statusCode() == 204) {
                // If the response is 204 No Content, return null (or handle it accordingly)
                return null;
            } else {
                // Handle other status codes if needed
                System.out.println("Unexpected status code: " + response.statusCode());
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        }


    }


    public static void main(String[] args) {

        //registracija na poslužitelju i provjera je li postavljen identifikator
        Double longitude = Utils.generateRandomDouble(15.87, 16.0);
        Double latitude = Utils.generateRandomDouble(45.75, 45.85);
        String ip = "127.0.0.1";
        Integer port = Utils.generateRandomInteger(8000, 65000);
        Boolean registered = register(latitude, longitude, ip, port);
        System.out.println("Sensor is registered: " + registered);
        System.out.println("Identifier: " + identifier);

        //generiranje ocitanja
        sensorReading = Utils.parseReading();
        System.out.println("My reading:" + sensorReading.toString());

        //kreiranje gRPC servera
        final RPCServer server = new RPCServer(new RPCService(), port);
        server.start();
        //server.blockUntilShutdown();


        while (true) {


            //trazenje informacija o najblizem susjedu za razmjenu ocitanja
            SensorDTO neighbourSensor = findClosestNeighbour();

            if (neighbourSensor != null) {
                System.out.println("Neighbour sensor: " + neighbourSensor.getLatitude() + ", " + neighbourSensor.getLongitude() + ", " + neighbourSensor.getIp() + ", " + neighbourSensor.getPort());
                //kreiranje gRPC klijenta
                RPCClient client = new RPCClient(neighbourSensor.getIp(), neighbourSensor.getPort());

                ReadingDTO rpcReading = client.requestReading();
                if (rpcReading != null) {
                    System.out.println("RPC reading:" + rpcReading.toString());
                } else {
                    System.out.println("Filed to get the reading.");
                }

            } else {
                System.out.println("No closest neighbour");
            }

          try {
            Thread.sleep(5000);
          } catch (InterruptedException e) {

          }

        }

    }

}
