package com.myplantdiary.enterprise.service;

import org.springframework.stereotype.Service;
import com.myplantdiary.enterprise.entity.Messages;

import java.util.List;

@Service
public interface MessagesService {
    List<Messages> getAllMessages();
    void save(Messages messages);
}
