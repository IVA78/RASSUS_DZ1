package hr.fer.tel.rassus.client.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Utils {

    public static ReadingDTO parseReading() {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get("C:\\Users\\38595\\Documents\\GitHub\\RASSUS_DZ1\\client\\readings.csv"));
        } catch (IOException e) {
            System.out.println("Failed to read CSV");
        }

        Random random = new Random();
        String randomLine = lines.get(random.nextInt(lines.size()+1));
        /*
        System.out.println("Random line: " + randomLine);
        for(String s : randomLine.split(",")) {
            System.out.print("|"+s+"|");
        }
        System.out.println();
         */

        int cnt = 0;

        HashMap<String, String> readingValues = new HashMap<>();
        randomLine = randomLine + "$,";
        for (String s : randomLine.split(",")) {
            switch (cnt) {
                case 0:
                    readingValues.put("temperature", s == "" ? "" : s.trim());
                    break;
                case 1:
                    readingValues.put("pressure", s == "" ? "" : s.trim());
                    break;
                case 2:
                    readingValues.put("humidity", s == "" ? "" : s.trim());
                    break;
                case 3:
                    readingValues.put("co", s == "" ? "" : s.trim());
                    break;
                case 4:
                    readingValues.put("no2", s == "" ? "" : s.trim());
                    break;
                case 5:
                    readingValues.put("so2", s == "" ? "" : s.trim());
                    break;
                default:
                    break;
            }
            cnt++;
        }


        Integer temperature = Integer.parseInt(readingValues.get("temperature"));
        Integer pressure = Integer.parseInt(readingValues.get("pressure"));
        Integer humidity = Integer.parseInt(readingValues.get("humidity"));
        Integer co = readingValues.get("co") == "" ? null : Integer.parseInt(readingValues.get("co"));
        Integer no2 = readingValues.get("no2") == "" ? null : Integer.parseInt(readingValues.get("no2"));
        Integer so2 = readingValues.get("so2") == "" ? null : Integer.parseInt(readingValues.get("so2"));

        return new ReadingDTO(temperature, pressure, humidity, co, no2, so2);
    }

    public static Double generateRandomDouble(Double min, Double max) {
        Random random = new Random();
        return min + (max - min) * random.nextDouble();
    }

    public static Integer generateRandomInteger(Integer min, Integer max) {
        Random random = new Random();
        return min + random.nextInt(max - min);
    }






}
