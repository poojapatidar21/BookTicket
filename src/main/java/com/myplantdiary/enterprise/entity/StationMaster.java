package com.myplantdiary.enterprise.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Station_master")
public class StationMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long station_id;
    private String station_name;

    //getters


    public Long getStation_id() {
        return station_id;
    }

    public String getStation_name() {
        return station_name;
    }

    public void setStation_id(Long station_id) {
        this.station_id = station_id;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }

    public StationMaster(String station_name) {
        this.station_name = station_name;
    }

    public StationMaster() {
    }
}
