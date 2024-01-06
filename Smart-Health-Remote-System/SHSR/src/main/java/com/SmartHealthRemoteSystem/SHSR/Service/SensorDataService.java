package com.SmartHealthRemoteSystem.SHSR.Service;

import com.SmartHealthRemoteSystem.SHSR.ReadSensorData.SensorData;
import com.SmartHealthRemoteSystem.SHSR.ReadSensorData.SensorDataRepository;
import com.SmartHealthRemoteSystem.SHSR.Repository.SHSRDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class SensorDataService {
    private final SHSRDAO<SensorData> sensorDataRepository;

    public SensorDataService() {
        sensorDataRepository=new SensorDataRepository();
    }

    @Autowired
    public SensorDataService(SHSRDAO<SensorData> sensorDataRepository) {
        this.sensorDataRepository = sensorDataRepository;
    }

    public String createSensorData() throws ExecutionException, InterruptedException {
        SensorData sensorData = new SensorData();
        return sensorDataRepository.save(sensorData);
    }

    public String deleteSensorData(String sensorId) throws ExecutionException, InterruptedException {
        return sensorDataRepository.delete(sensorId);
    }

    public SensorData getSensorData(String sensorId) throws ExecutionException, InterruptedException {
        return sensorDataRepository.get(sensorId);
    }

    public String updateSensorData(SensorData sensorData) throws ExecutionException, InterruptedException {
        return sensorDataRepository.update(sensorData);
    }

    public String stringSensorData(String sensorId) throws ExecutionException, InterruptedException {
        SensorData sensorData=sensorDataRepository.get(sensorId);
        return sensorData.toString();
    }
}
