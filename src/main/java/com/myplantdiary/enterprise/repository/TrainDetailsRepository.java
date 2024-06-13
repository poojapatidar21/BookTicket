package com.myplantdiary.enterprise.repository;

import com.myplantdiary.enterprise.entity.TrainDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TrainDetailsRepository extends JpaRepository<TrainDetails, Long> {

}
