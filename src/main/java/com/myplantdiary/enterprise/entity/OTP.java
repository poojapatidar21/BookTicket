package com.myplantdiary.enterprise.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "otp")
public class OTP {
    @Id
    private String email;
    private int otp;
    LocalDateTime time;
    boolean register;

    public String getEmail() {
        return email;
    }
    public int getOtp() {
        return otp;
    }
    public LocalDateTime getTime() {
        return time;
    }
    public boolean getRegister() {
        return register;
    }


    public void setEmail(String email) {
        this.email = email;
    }
    public void setTime(LocalDateTime time) {
        this.time = time;
    }
    public void setOtp(int otp) {
        this.otp=otp;
    }
    public void setRegister(boolean register) {
        this.register = register;
    }
}