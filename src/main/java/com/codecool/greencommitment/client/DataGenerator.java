package com.codecool.greencommitment.client;

import com.codecool.greencommitment.common.Measurement;
import com.codecool.greencommitment.common.MoistureMeasurement;
import com.codecool.greencommitment.common.TemperatureMeasurement;

import java.time.Instant;
import java.util.Random;

public class DataGenerator {
    private Type type;


    private Random random = new Random();

    public DataGenerator(Type type) {
        this.type = type;
    }


    public Measurement createData() {
        Measurement measure = null;
        long time = Instant.now().toEpochMilli();
        time += 2000;
        String unitOfMeasurement;
        int unit;
        switch (type) {
            case TEMPERATURE:
                unit = 15 + random.nextInt(30);
                unitOfMeasurement = "Celsius";
                measure = new TemperatureMeasurement(time, unit, unitOfMeasurement);
                return measure;
            case MOISTURE:
                unit = 45 + random.nextInt(70);
                unitOfMeasurement = "%";
                measure = new MoistureMeasurement(time, unit, unitOfMeasurement);
                return measure;
        }
        return measure;
    }

}
