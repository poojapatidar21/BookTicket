package com.myplantdiary.enterprise.repository;

import com.myplantdiary.enterprise.entity.TrainMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainRepository extends JpaRepository<TrainMaster, Long> {
    List<TrainMaster> findByDepartureAndDestination(String departure, String destination);
    TrainMaster findByTno(Long tno);
}