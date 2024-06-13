package com.myplantdiary.enterprise.service;

import com.myplantdiary.enterprise.entity.TrainMaster;
import com.myplantdiary.enterprise.entity.User;
import com.myplantdiary.enterprise.entity.UserData;
import com.myplantdiary.enterprise.repository.*;
import com.myplantdiary.enterprise.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final UserDataRepository userDataRepository;
    private final StationDataRepository stationDataRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserDataRepository userDataRepository, StationDataRepository stationDataRepository) {
        this.userRepository = userRepository;
        this.userDataRepository = userDataRepository;
        this.stationDataRepository = stationDataRepository;
    }

public List<User> getAllEntities(){
        return userRepository.findAll();
}

    public void save(User user) {
        userRepository.save(user);
    }
    public UserData findByBookingId(String bookingId) {
        return userDataRepository.findByBookingId(bookingId);
    }


    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void saveUserData(UserData userData) {
        userDataRepository.save(userData);
    }

    public List<UserData> getAllUserData() {
        return userDataRepository.findAll();
    }
    public List<TrainMaster> getAllTrainData() {
        return stationDataRepository.findAll();
    }

    public List<UserData> getUniqueUserDataForDestination(String destination) {
        return userDataRepository.findByDestination(destination);
    }
    public List<UserData> getUserDataByEmail(String email){
        return userDataRepository.findByEmail(email);
    }
    public List<Object[]> getDestinationStats() {
        return userDataRepository.findDestinationStats();
    }

    public List<Object[]> getHighestEarningStation() {
        return userDataRepository.findHighestEarningStation();
    }

    public List<Object[]> getTotalPassengersAndEarnings() {
        return userDataRepository.getTotalPassengersAndEarnings();
    }


}
