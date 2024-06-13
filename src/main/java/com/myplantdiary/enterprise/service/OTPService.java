package com.myplantdiary.enterprise.service;
import com.myplantdiary.enterprise.repository.OTPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.myplantdiary.enterprise.entity.OTP;
import java.time.LocalDateTime;
@Service
public class OTPService {

    @Autowired
    private OTPRepository OTPRepository;

    public String getEmail(String email) {
        OTP Otp = OTPRepository.findByEmail(email);
        return (Otp != null) ? Otp.getEmail() : null;
    }

    public void saveOtp(String email, LocalDateTime time, int otp, boolean register) {
        OTP Otp = OTPRepository.findByEmail(email);

        if (Otp == null) {
            Otp = new OTP();
            Otp.setEmail(email);
        }
        Otp.setOtp(otp);
        Otp.setTime(time);
        Otp.setRegister(register);
        OTPRepository.save(Otp);
    }
    public boolean isRegistered(String email) {
        OTP Otp = OTPRepository.findByEmail(email);
        if (Otp == null) {
            return false;
        }
        return Otp.getRegister();
    }
    public OTP getOTP(String email) {
        return OTPRepository.findByEmail(email);
    }
    public void Register(String email, boolean register) {
        OTP Otp = OTPRepository.findByEmail(email);
        if (Otp == null) {
            return;
        }
        Otp.setRegister(register);
    }
}
