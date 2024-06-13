package com.myplantdiary.enterprise.service;
import com.myplantdiary.enterprise.entity.Messages;
import com.myplantdiary.enterprise.repository.MessagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MessagesServiceImpl implements MessagesService {
    private final MessagesRepository messagesRepository;

    @Autowired
    public MessagesServiceImpl(MessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }
    @Override
    public List<Messages> getAllMessages() {
        return messagesRepository.findAll();
    }
    public void save(Messages messages) {
        messagesRepository.save(messages);
    }
}
