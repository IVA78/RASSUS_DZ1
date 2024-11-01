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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

public class Application {
    private static long startTime = System.nanoTime();

    private static final Logger logger = Logger.getLogger(Application.class.getName());
    private static String server_url = "http://localhost:8090";
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static HttpClient client = HttpClient.newHttpClient();
    private static String identifier = null;
    private static ReadingDTO sensorReading = null;

    public static ReadingDTO getSensorReading() {
        return sensorReading;
    }

    public static long getStartTime() {
        return startTime;
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
            throw new RuntimeException("Error in serialization with JSON format!");
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(server_url + "/server/registerSensor"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Status code: " + response.statusCode());
            if (response.statusCode() == 201) {
                identifier = response.headers().firstValue("Location").orElse(null);
                return identifier != null;
            } else {
                throw new RuntimeException("Filed to register, status code: " + response.statusCode());
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
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

    public static Boolean saveReading(Integer temperature, Integer pressure, Integer humidity, Integer co, Integer no2, Integer so2) {

        HashMap<String, Integer> readingMap = new HashMap<>();
        readingMap.put("temperature", temperature);
        readingMap.put("pressure", pressure);
        readingMap.put("humidity", humidity);
        readingMap.put("co", co);

        if (so2 != null) {
            readingMap.put("so2", so2);
        }
        if (no2 != null) {
            readingMap.put("no2", no2);
        }

        String json;
        try {
            json = objectMapper.writeValueAsString(readingMap);
        } catch (Exception e) {
            throw new RuntimeException("Error in serialization with JSON format!");
        }


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(server_url + "/readings/saveReading/" + identifier))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //System.out.println("Status code: " + response.statusCode());
            if (response.statusCode() == 201) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return false;
        }

    }

    public static ReadingDTO calibrateReading(ReadingDTO sensorReading, ReadingDTO rpcReading) {

        ReadingDTO calibratedReading = new ReadingDTO();

        Integer calibratedTemperature = (sensorReading.getTemperature() + rpcReading.getTemperature()) / 2;
        Integer calibratedPressure = (sensorReading.getPressure() + rpcReading.getPressure()) / 2;
        Integer calibratedHumidity = (sensorReading.getHumidity() + rpcReading.getHumidity()) / 2;
        Integer calibratedCo = (sensorReading.getCo() + rpcReading.getCo()) / 2;

        Integer calibratedNo2 = null;
        if (sensorReading.getNo2() != null && sensorReading.getNo2() != 0 && rpcReading.getNo2() != null && rpcReading.getNo2() != 0 & rpcReading.getSo2() != -1) {
            calibratedNo2 = (sensorReading.getNo2() + rpcReading.getNo2()) / 2;
        } else if (sensorReading.getNo2() != null && sensorReading.getNo2() != 0) {
            calibratedNo2 = sensorReading.getNo2();
        } else if (rpcReading.getNo2() != null && rpcReading.getNo2() != 0 & rpcReading.getSo2() != -1) {
            calibratedNo2 = rpcReading.getNo2();
        }

        Integer calibratedSo2 = null;
        if (sensorReading.getSo2() != null && sensorReading.getSo2() != 0 && rpcReading.getSo2() != null && rpcReading.getSo2() != 0 && rpcReading.getSo2() != -1) {
            calibratedSo2 = (sensorReading.getSo2() + rpcReading.getSo2()) / 2;
        } else if (sensorReading.getSo2() != null && sensorReading.getSo2() != 0) {
            calibratedSo2 = sensorReading.getSo2();
        } else if (rpcReading.getSo2() != null && rpcReading.getSo2() != 0 & rpcReading.getSo2() != -1) {
            calibratedSo2 = rpcReading.getSo2();
        }

        calibratedReading.setTemperature(calibratedTemperature);
        calibratedReading.setPressure(calibratedPressure);
        calibratedReading.setHumidity(calibratedHumidity);
        calibratedReading.setCo(calibratedCo);
        calibratedReading.setNo2(calibratedNo2);
        calibratedReading.setSo2(calibratedSo2);


        return calibratedReading;

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

        //prekiranje rada ako je registracija neuspješna
        if(!registered){
            return;
        }

        //kreiranje gRPC servera
        final RPCServer server = new RPCServer(new RPCService(), port);
        server.start();

        //generiranje ocitanja
        sensorReading = Utils.parseReading();
        logger.info("My reading:" + sensorReading.toString());


        //trazenje informacija o najblizem susjedu za razmjenu ocitanja - NECE SE MIJENJATI TIJEKOM VREMENA
        logger.info("Finding closest neighbour.");
        SensorDTO neighbourSensor = findClosestNeighbour();


        while (true) {

            //generiranje ocitanja
            sensorReading = Utils.parseReading();
            logger.info("My reading:" + sensorReading.toString());


            if (neighbourSensor != null) {
                logger.info("Closest neighbour found!");
                logger.info("Neighbour sensor: " + neighbourSensor.getLatitude() + ", " + neighbourSensor.getLongitude() + ", " + neighbourSensor.getIp() + ", " + neighbourSensor.getPort());

                //kreiranje gRPC klijenta
                logger.info("Creating RPC client.");
                RPCClient client = new RPCClient(neighbourSensor.getIp(), neighbourSensor.getPort());

                logger.info("Sending reading request to closest neighbour");
                ReadingDTO rpcReading = client.requestReading();
                if (rpcReading != null) {
                    logger.info("RPC reading:" + rpcReading.toString());

                    //kalibracija rjesenja i slanje na posluzitelj
                    ReadingDTO calibratedReading = calibrateReading(sensorReading, rpcReading);
                    Boolean saved = saveReading(calibratedReading.getTemperature(), calibratedReading.getPressure(), calibratedReading.getHumidity(), calibratedReading.getCo(), calibratedReading.getNo2(), calibratedReading.getSo2());
                    logger.info("Calibrated reading saved: " + saved);


                } else {
                    logger.info("Failed to get the neighbour reading.");
                    //slanje vlastitih ocitanja na posluzitelj
                    Boolean saved = saveReading(sensorReading.getTemperature(), sensorReading.getPressure(), sensorReading.getHumidity(), sensorReading.getCo(), sensorReading.getNo2(), sensorReading.getSo2());
                    logger.info("Sensor reading saved: " + saved);
                }

            } else {
                logger.info("No closest neighbour found.");
                //slanje vlastitih ocitanja na posluzitelj
                Boolean saved = saveReading(sensorReading.getTemperature(), sensorReading.getPressure(), sensorReading.getHumidity(), sensorReading.getCo(), sensorReading.getNo2(), sensorReading.getSo2());
                logger.info("Sensor reading saved: " + saved);

            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {

            }

        }

    }

}
