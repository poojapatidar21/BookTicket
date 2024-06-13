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
public interface TrainService1 {

    List<TrainDetails> getAllTrains();
    void saveDataFromCsv(MultipartFile file);

}