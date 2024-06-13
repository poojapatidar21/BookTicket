package com.myplantdiary.enterprise.service;

import com.myplantdiary.enterprise.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    void save(User user);
}
