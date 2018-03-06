package com.codecool.greencommitment.client;

import com.codecool.greencommitment.common.Measurement;
import com.codecool.greencommitment.common.MoistureMeasurement;
import com.codecool.greencommitment.common.TemperatureMeasurement;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataGenerator {
    private int numberOfMeasurement;
    private int min;
    private int max;
    private String type;



    private Random random = new Random();

    DataGenerator(int numberOfMeasurement, int min, int max, String type) {
        this.numberOfMeasurement = numberOfMeasurement;
        this.min = min;
        this.max = max;
        this.type = type;
    }


    public List<Measurement> createData() {
        Measurement measure = null;
        List<Measurement> list = new ArrayList<>();
        long time = Instant.now().toEpochMilli();
        for (int i = 0; i < numberOfMeasurement ; i++) {
            time += 2000;
            int unit = random.nextInt(max-min) + min;
            String unitOfMeasurement;
            if (type.equals("temp")) {
                unitOfMeasurement = "Celsius";
                measure = new TemperatureMeasurement(time, unit,unitOfMeasurement );
            }
            else if (type.equals("moisture")) {
                unitOfMeasurement = "%";
                measure = new MoistureMeasurement(time, unit, unitOfMeasurement);
            }

           list.add(measure);
        }
        return list;
    }

    public Measurement getRandomMeasurement() {
        Measurement result = null;
        String unitOfMeasurement;
        int unit = random.nextInt(max-min) + min;
        long time = Instant.now().toEpochMilli();

        if (type.equals("temp")) {
            unitOfMeasurement = "Celsius";
            result = new TemperatureMeasurement(time, unit,unitOfMeasurement );
        }
        else if (type.equals("moisture")) {
            unitOfMeasurement = "%";
            result = new MoistureMeasurement(time, unit, unitOfMeasurement);
        }
        return result;
    }
}
