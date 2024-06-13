package com.myplantdiary.enterprise.repository;

import com.myplantdiary.enterprise.entity.User;
import com.myplantdiary.enterprise.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, Long> {
    // Additional methods if needed
//    Optional<UserData> findByFirstName(String firstName);

    UserData findByBookingId(String bookingId);
    List<UserData> findByDestination(String destination);
    List<UserData> findByEmail(String email);

    @Query("SELECT u.destination, SUM(u.no_tickets) as totalPassengers, SUM((u.no_tickets)*(u.price1 + u.price2)) as totalEarnings FROM UserData u where u.payment_done=true GROUP BY u.destination ")
    List<Object[]> findDestinationStats();

    @Query("SELECT u.destination, SUM((u.no_tickets)*(u.price1 + u.price2)) as totalEarnings FROM UserData u where u.payment_done=true GROUP BY u.destination ORDER BY totalEarnings DESC LIMIT 1")
    List<Object[]> findHighestEarningStation();

    @Query("SELECT SUM(u.no_tickets), SUM((u.no_tickets)*(u.price1 + u.price2)) FROM UserData u where u.payment_done=true")
    List<Object[]> getTotalPassengersAndEarnings();
}

