package com.myplantdiary.enterprise.repository;

import com.myplantdiary.enterprise.entity.TrainMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationDataRepository extends JpaRepository<TrainMaster, Long> {
    TrainMaster findByTno (long tno);

}
