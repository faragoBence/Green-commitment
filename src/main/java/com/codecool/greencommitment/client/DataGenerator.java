package com.codecool.greencommitment.client;

import com.codecool.greencommitment.common.Measurement;
import com.codecool.greencommitment.common.MoistureMeasurement;
import com.codecool.greencommitment.common.TemperatureMeasurement;
import com.codecool.greencommitment.gui.WindowManager;

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
        String unitOfMeasurement;
        int unit;
        switch (type) {
            case TEMPERATURE:
                unit = 15 + random.nextInt(25);
                unitOfMeasurement = "Celsius";
                measure = new TemperatureMeasurement(time, unit, unitOfMeasurement);
                WindowManager.setClientList(measure);
                return measure;
            case MOISTURE:
                unit = 20 + random.nextInt(50);
                unitOfMeasurement = "%";
                measure = new MoistureMeasurement(time, unit, unitOfMeasurement);
                WindowManager.setClientList(measure);
                return measure;
        }
        return measure;
    }

}
