package com.myplantdiary.enterprise.service;

import com.myplantdiary.enterprise.entity.TrainDetails;
import com.myplantdiary.enterprise.repository.TrainDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

@Service
public class TrainServiceImpl implements TrainService1 {

    @Autowired
    private TrainDetailsRepository trainDetailsRepository;

    @Override
    public List<TrainDetails> getAllTrains() {
        // Implement the logic to get all trains from the repository
        return trainDetailsRepository.findAll();
    }

    @Override
    public void saveDataFromCsv(MultipartFile file) {
        try (Reader reader = new InputStreamReader(file.getInputStream());
             CSVReader csvReader = new CSVReader(reader)) {

            String[] nextRecord;
            csvReader.skip(1); // Skip header row

            while ((nextRecord = csvReader.readNext()) != null) {
                TrainDetails trainDetails = new TrainDetails();
                trainDetails.setTrainName(nextRecord[0]);
                trainDetails.setStationName(nextRecord[1]);
                trainDetails.setTime(nextRecord[2]);
                trainDetails.setFare(Double.parseDouble(nextRecord[3]));

                trainDetailsRepository.save(trainDetails);
            }

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
            // Handle exceptions appropriately
        }
    }
}
