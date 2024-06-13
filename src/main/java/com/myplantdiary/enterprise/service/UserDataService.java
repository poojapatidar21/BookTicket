package com.myplantdiary.enterprise.service;

import com.myplantdiary.enterprise.entity.UserData;
import com.myplantdiary.enterprise.repository.UserDataRepository;

import java.util.List;

public class UserDataService {
    private final UserDataRepository userDataRepository;

    public UserDataService(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    public List<UserData> getUniqueUserDataForDestination(String destination) {
        return userDataRepository.findByDestination(destination);
    }

    public List<Object[]> getDestinationStats() {
        return userDataRepository.findDestinationStats();
    }

    public List<Object[]> getHighestEarningStation() {
        return userDataRepository.findHighestEarningStation();
    }


}
