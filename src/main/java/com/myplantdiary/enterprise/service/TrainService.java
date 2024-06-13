package com.myplantdiary.enterprise.service;

import com.myplantdiary.enterprise.entity.TrainMaster;
import com.myplantdiary.enterprise.repository.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TrainService {
    private final TrainRepository trainRepository;

    @Autowired
    public TrainService(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }
    public List<TrainMaster> findTrainsByStations(String departure, String destination){
        List<TrainMaster> trains = trainRepository.findByDepartureAndDestination(departure, destination);
        return trains;
    }
    public TrainMaster findbyTno(Long tno)
    {
        return trainRepository.findByTno(tno);
    }
}
