package com.myplantdiary.enterprise.entity;

import jakarta.persistence.*;

import java.time.*;
import com.myplantdiary.enterprise.entity.User;

@Entity
@Table(name = "user_data")
public class UserData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String first_name;
    private String last_name;
    private String departure;
    private String destination;
    private Long no_tickets;
    private String payment_method;

    private LocalDateTime submission_time;
    private LocalTime departureTime1;
    private LocalTime departureTime2;
    private LocalDate departureDate1;
    private LocalDate departureDate2;
    private boolean returnExist;
    private String bookingId;
    private boolean payment_done=false;
    private int price1;
    private int price2;
    private String email;
    private String tname;

    // getters

    public Long getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getDeparture() {
        return departure;
    }

    public String getDestination() {
        return destination;
    }

    public Long getNo_tickets() {
        return no_tickets;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public LocalDateTime getSubmission_time() {
        return submission_time;
    }
    public LocalTime getDepartureTime1() {
        return departureTime1;
    }
    public LocalTime getDepartureTime2() {
        return departureTime2;
    }
    public LocalDate getDepartureDate1() {
        return departureDate1;
    }
    public LocalDate getDepartureDate2() {
        return departureDate2;
    }
    public int getPrice1(){return price1;}
    public int getPrice2(){return price2;}
    public String getTname(){return tname;}
    public String getEmail(){return email;}

    public boolean isReturnExist() {
        return returnExist;
    }

    public String getBookingId() {
        return bookingId;
    }

    public boolean isPayment_done() {
        return payment_done;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setNo_tickets(Long no_tickets) {
        this.no_tickets = no_tickets;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public void setSubmission_time(LocalDateTime submission_time) {
        this.submission_time = submission_time;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public void setPayment_done(boolean payment_done) {
        this.payment_done = payment_done;
    }
    public void setDepartureTime1(LocalTime departureTime1) {
        this.departureTime1 = departureTime1;
    }
    public void setDepartureTime2(LocalTime departureTime2) {
        this.departureTime2 = departureTime2;
    }
    public void setDepartureDate1(LocalDate departureDate1) {
        this.departureDate1 = departureDate1;
    }
    public void setDepartureDate2(LocalDate departureDate2) {
        this.departureDate2 = departureDate2;
    }
    public void setReturnExist(boolean returnExist){
        this.returnExist = returnExist;
    }

    public void setPrice1(int price1){
        this.price1 = price1;
    }
    public void setPrice2(int price2){
        this.price2 = price2;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }
    //constructors



    public UserData(String first_name, String last_name, String departure, String destination, Long no_tickets, String payment_method, LocalDateTime submission_time, String bookingId, boolean payment_done, LocalTime departureTime1, LocalTime departureTime2, LocalDate departureDate1, LocalDate departureDate2, boolean returnExist, int price1, int price2, String email, String tname) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.departure = departure;
        this.destination = destination;
        this.no_tickets = no_tickets;
        this.payment_method = payment_method;
        this.submission_time = submission_time;
        this.bookingId = bookingId;
        this.payment_done = payment_done;
        this.departureTime1 = departureTime1;
        this.departureTime2 = departureTime2;
        this.departureDate1 = departureDate1;
        this.departureDate2 = departureDate2;
        this.returnExist = returnExist;
        this.price1 = price1;
        this.price2 = price2;
        this.tname = tname;
        this.email = email;
    }

    public UserData() {
    }
}
