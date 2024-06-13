package com.myplantdiary.enterprise.repository;
import com.myplantdiary.enterprise.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OTPRepository extends JpaRepository<OTP, Long> {
    OTP findByEmail(String email);
    // Additional methods if needed
}
