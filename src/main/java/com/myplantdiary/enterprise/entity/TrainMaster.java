package com.myplantdiary.enterprise.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalTime;

@Entity
public class TrainMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tno;
    private String T_name;
    private String type;
    private String departure;
    private String destination;
    private LocalTime time;
    private int fare;

    public Long getTno() {
        return tno;
    }

    public String getT_name() {
        return T_name;
    }

    public String getDeparture() {
        return departure;
    }

    public String getDestination() {
        return destination;
    }
    public LocalTime getTime() {
        return time;
    }

    public int getFare() { return fare; }
    public String getType() { return type; }

    public void setTNo(Long tno) {
        this.tno = tno;
    }

    public void setT_name(String t_name) {
        this.T_name = t_name;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
    public void setFare(int fare) { this.fare = fare; }
    public void setType(String type) { this.type = type; }

    public TrainMaster(Long tno, String t_name, String departure, String destination, int fare, String type, LocalTime time) {
        this.tno = tno;
        this.T_name = t_name;
        this.departure = departure;
        this.destination = destination;
        this.fare = fare;
        this.type = type;
        this.time = time;
    }

    public TrainMaster() {
    }
}
