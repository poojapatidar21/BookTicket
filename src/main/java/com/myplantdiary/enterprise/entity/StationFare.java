package com.myplantdiary.enterprise.entity;

import jakarta.persistence.*;

@Entity

public class StationFare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float fare;
    @ManyToOne
    @JoinColumn(name = "Station_master_id_1") // Specify the foreign key column
    private StationMaster destination;

    @ManyToOne
    @JoinColumn(name = "Station_master_id_2") // Specify the foreign key column
    private StationMaster departure;

    public Long getId() {
        return id;
    }

    public float getFare() {
        return fare;
    }

    public StationMaster getDestination() {
        return destination;
    }

    public StationMaster getDeparture() {
        return departure;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFare(float fare) {
        this.fare = fare;
    }

    public void setDestination(StationMaster destination) {
        this.destination = destination;
    }

    public void setDeparture(StationMaster departure) {
        this.departure = departure;
    }

    public StationFare(float fare, StationMaster destination, StationMaster departure) {
        this.destination = destination;
        this.departure = departure;
        this.fare = fare;
    }

    public StationFare() {
    }
}
